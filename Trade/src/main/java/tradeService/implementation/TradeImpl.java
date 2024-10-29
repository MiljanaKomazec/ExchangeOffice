package tradeService.implementation;
import java.math.BigDecimal;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.CurrencyExchangeDto;
import api.dto.TradeResponseBankAccountDto;
import api.dto.TradeResponseCryptoDto;
import api.dto.bankAccount.GetSingleBankAccountDto;
import api.dto.bankAccount.UpdateBankAccountDto;
import api.dto.cryptoWallet.GetSingleCryptoWallet;
import api.dto.cryptoWallet.UpdateCryptoWallet;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CryptoWalletProxy;
import api.feignProxies.CurrencyExchangeProxy;
import api.services.TradeService;
import jakarta.servlet.http.HttpServletRequest;
import tradeService.model.TradeModel;
import tradeService.repository.TradeRepository;
import util.exceptions.NoDataFoundException;
import util.exceptions.ValidationBrokenException;

@RestController
public class TradeImpl implements TradeService {

	@Autowired
	private TradeRepository repo;
	
	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;
	
	@Override
	public ResponseEntity<?> getConversion(String from, String to, BigDecimal quantity, HttpServletRequest request) {
		
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty()) {
			  throw new ValidationBrokenException("You are not allowed to perform this operation.");
		}
		
		String base64UserNameAndPassword = authHeader.split(" ")[1];
		byte[] decodedBytes = Base64.getDecoder().decode(base64UserNameAndPassword);
		String decodedString = new String(decodedBytes);
		String email = decodedString.split(":")[0];
		
		if(from.equals("RSD") || from.equals("CHF") || from.equals("GBP") || from.equals("EUR") || from.equals("USD"))
		{
			ResponseEntity<GetSingleBankAccountDto> bankAccountResponse = bankAccountProxy.getAccount(email);
			if (! bankAccountResponse.getStatusCode().is2xxSuccessful()) {
				throw new NoDataFoundException();
			}
			
			String fromOriginal = from;
			double quantityToChange = quantity.doubleValue();
		
			GetSingleBankAccountDto bankAccount = bankAccountResponse.getBody();
			double availableAmount = 0;
			if (from.equals("RSD")) {
				availableAmount = bankAccount.getAmountRsd();
			} else if (from.equals("EUR")) {
				availableAmount = bankAccount.getAmountEur();
			} else if (from.equals("USD")) {
				availableAmount = bankAccount.getAmountUsd();
			} else if (from.equals("CHF")) {
				availableAmount = bankAccount.getAmountChf();
			} else if (from.equals("GBP")) {
				availableAmount = bankAccount.getAmountGbp();
			} else {
				throw new NoDataFoundException();
			}
			
			if (!to.equals("BTC") && !to.equals("LTC") && !to.equals("XMR")) {
			    throw new ValidationBrokenException("The entered currency is not valid.");
			}
			
			if (availableAmount < quantity.doubleValue()) {
				throw new ValidationBrokenException("Not enough money");
			}
			
			if (from.equals("RSD") || from.equals("CHF") || from.equals("GBP")) {
				String prefferedCurrency = "EUR";
				ResponseEntity<CurrencyExchangeDto> currencyExchangeResponse = proxy.getExchange(from, prefferedCurrency);
				if (! currencyExchangeResponse.getStatusCode().is2xxSuccessful()) {
					throw new NoDataFoundException();
				}
				
				quantityToChange = quantityToChange * currencyExchangeResponse.getBody().getExchangeValue().doubleValue();
				from = prefferedCurrency;
			}
			
			TradeModel tradeModel = repo.findByFromAndTo(from, to);
			
			double quantityInCrypto = quantityToChange * tradeModel.getExchangeValue().doubleValue();
			
			ResponseEntity<GetSingleCryptoWallet> cryptoWalletResponse = cryptoWalletProxy.getWallet(email);
			if (! cryptoWalletResponse.getStatusCode().is2xxSuccessful()) {
				throw new NoDataFoundException();
			}
		
			GetSingleCryptoWallet cryptoWallet = cryptoWalletResponse.getBody();
			if (to.equals("BTC")) {
				cryptoWallet.setAmountBTC(cryptoWallet.getAmountBTC() + quantityInCrypto);
			} else if (to.equals("LTC")) {
				cryptoWallet.setAmountLTC(cryptoWallet.getAmountLTC() + quantityInCrypto);
			} else if (to.equals("XMR")) {
				cryptoWallet.setAmountXMR(cryptoWallet.getAmountXMR() + quantityInCrypto);
			} else {
				throw new NoDataFoundException();
			}
			
			if (fromOriginal.equals("RSD")) {
				bankAccount.setAmountRsd(bankAccount.getAmountRsd() - quantity.doubleValue());
			} else if (fromOriginal.equals("EUR")) {
				bankAccount.setAmountEur(bankAccount.getAmountEur() - quantity.doubleValue());
			} else if (fromOriginal.equals("USD")) {
				bankAccount.setAmountUsd(bankAccount.getAmountUsd() - quantity.doubleValue());
			} else if (fromOriginal.equals("CHF")) {
				bankAccount.setAmountChf(bankAccount.getAmountChf() - quantity.doubleValue());
			} else if (fromOriginal.equals("GBP")) {
				bankAccount.setAmountGbp(bankAccount.getAmountGbp() - quantity.doubleValue());
			} else {
				throw new NoDataFoundException();
			}
			
			UpdateBankAccountDto updateAccountDto = new UpdateBankAccountDto();
			updateAccountDto.setAmountRsd(bankAccount.getAmountRsd());
			updateAccountDto.setAmountEur(bankAccount.getAmountEur());
			updateAccountDto.setAmountUsd(bankAccount.getAmountUsd());
			updateAccountDto.setAmountChf(bankAccount.getAmountChf());
			updateAccountDto.setAmountGbp(bankAccount.getAmountGbp());
			updateAccountDto.setEmail(bankAccount.getEmail());
			updateAccountDto.setId(bankAccount.getId());
			
			ResponseEntity<?> updateAccountResponse = bankAccountProxy.updateAccount(updateAccountDto);
			if (! updateAccountResponse.getStatusCode().is2xxSuccessful()) {
				return updateAccountResponse;
			}
			
			UpdateCryptoWallet updateWalletDto = new UpdateCryptoWallet();
			updateWalletDto.setAmountBTC(cryptoWallet.getAmountBTC());
			updateWalletDto.setAmountLTC(cryptoWallet.getAmountLTC());
			updateWalletDto.setAmountXMR(cryptoWallet.getAmountXMR());
			updateWalletDto.setEmail(cryptoWallet.getEmail());
			updateWalletDto.setId(cryptoWallet.getId());
			
			ResponseEntity<?> updateWalletResponse = cryptoWalletProxy.updateWalet(updateWalletDto);
			if (! updateWalletResponse.getStatusCode().is2xxSuccessful()) {
				return updateWalletResponse;
			}
			
			TradeResponseCryptoDto responseDto = new TradeResponseCryptoDto();
			responseDto.setMessage("Uspešno je izvršena razmena " + from + ": " + quantity + " za " + to + ": " + quantityInCrypto
					+ ". " + " Stanje crypto novčanika: " );		
			responseDto.setAmountBTC(cryptoWallet.getAmountBTC());
			responseDto.setAmountLTC(cryptoWallet.getAmountLTC());
			responseDto.setAmountXMR(cryptoWallet.getAmountXMR());
		
			
			return ResponseEntity.ok(responseDto);
		} else if (from.equals("BTC") || from.equals("LTC") || from.equals("XMR")) {
			double quantityToChange = quantity.doubleValue();
			double quantityInFiat;
			
			ResponseEntity<GetSingleCryptoWallet> cryptoWalletResponse = cryptoWalletProxy.getWallet(email);
			if (! cryptoWalletResponse.getStatusCode().is2xxSuccessful()) {
				throw new NoDataFoundException();
			}
			
			GetSingleCryptoWallet cryptoWallet = cryptoWalletResponse.getBody();
			double availableAmount = 0;
			if (from.equals("BTC")) {
				availableAmount = cryptoWallet.getAmountBTC();
			} else if (from.equals("LTC")) {
				availableAmount = cryptoWallet.getAmountLTC();
			} else if (from.equals("XMR")) {
				availableAmount = cryptoWallet.getAmountXMR();
			} else {
				throw new NoDataFoundException();
			}
			
			if(!to.equals("RSD") && !to.equals("CHF") && !to.equals("GBP") && !to.equals("EUR") && !to.equals("USD")) {
				throw new ValidationBrokenException("The entered currency is not valid.");
			}
			
			if (availableAmount < quantity.doubleValue()) {
				throw new ValidationBrokenException("Not enough money");
			}
			
			if (to.equals("RSD") || to.equals("CHF") || to.equals("GBP")) {
				String prefferedCurrency = "EUR";
				
				TradeModel tradeModel = repo.findByFromAndTo(from, prefferedCurrency);
				double quantityInEUR = quantityToChange * tradeModel.getExchangeValue().doubleValue();
				
				ResponseEntity<CurrencyExchangeDto> currencyExchangeResponse = proxy.getExchange(prefferedCurrency, to);
				if (! currencyExchangeResponse.getStatusCode().is2xxSuccessful()) {
					throw new NoDataFoundException();
				}
				
				quantityInFiat = quantityInEUR * currencyExchangeResponse.getBody().getExchangeValue().doubleValue();
			} else {
				TradeModel tradeModel = repo.findByFromAndTo(from, to);
				quantityInFiat = quantityToChange * tradeModel.getExchangeValue().doubleValue();
			}
			
			if (from.equals("BTC")) {
				cryptoWallet.setAmountBTC(cryptoWallet.getAmountBTC() - quantity.doubleValue());
			} else if (from.equals("LTC")) {
				cryptoWallet.setAmountLTC(cryptoWallet.getAmountLTC() - quantity.doubleValue());
			} else if (from.equals("XMR")) {
				cryptoWallet.setAmountXMR(cryptoWallet.getAmountXMR() - quantity.doubleValue());
			} else {
				throw new NoDataFoundException();
			}
			
			ResponseEntity<GetSingleBankAccountDto> bankAccountResponse = bankAccountProxy.getAccount(email);
			if (! bankAccountResponse.getStatusCode().is2xxSuccessful()) {
				throw new NoDataFoundException();
			}
		
			GetSingleBankAccountDto bankAccount = bankAccountResponse.getBody();
			if (to.equals("RSD")) {
				bankAccount.setAmountRsd(bankAccount.getAmountRsd() + quantityInFiat);
			} else if (to.equals("EUR")) {
				bankAccount.setAmountEur(bankAccount.getAmountEur() + quantityInFiat);
			} else if (to.equals("USD")) {
				bankAccount.setAmountUsd(bankAccount.getAmountUsd() + quantityInFiat);
			} else if (to.equals("CHF")) {
				bankAccount.setAmountChf(bankAccount.getAmountChf() + quantityInFiat);
			} else if (to.equals("GBP")) {
				bankAccount.setAmountGbp(bankAccount.getAmountGbp() + quantityInFiat);
			} else {
				throw new NoDataFoundException();
			}
			
			
			UpdateBankAccountDto updateAccountDto = new UpdateBankAccountDto();
			updateAccountDto.setAmountRsd(bankAccount.getAmountRsd());
			updateAccountDto.setAmountEur(bankAccount.getAmountEur());
			updateAccountDto.setAmountUsd(bankAccount.getAmountUsd());
			updateAccountDto.setAmountChf(bankAccount.getAmountChf());
			updateAccountDto.setAmountGbp(bankAccount.getAmountGbp());
			updateAccountDto.setEmail(bankAccount.getEmail());
			updateAccountDto.setId(bankAccount.getId());
			
			ResponseEntity<?> updateAccountResponse = bankAccountProxy.updateAccount(updateAccountDto);
			if (! updateAccountResponse.getStatusCode().is2xxSuccessful()) {
				return updateAccountResponse;
			}
			
			UpdateCryptoWallet updateWalletDto = new UpdateCryptoWallet();
			updateWalletDto.setAmountBTC(cryptoWallet.getAmountBTC());
			updateWalletDto.setAmountLTC(cryptoWallet.getAmountLTC());
			updateWalletDto.setAmountXMR(cryptoWallet.getAmountXMR());
			updateWalletDto.setEmail(cryptoWallet.getEmail());
			updateWalletDto.setId(cryptoWallet.getId());
			
			ResponseEntity<?> updateWalletResponse = cryptoWalletProxy.updateWalet(updateWalletDto);
			if (! updateWalletResponse.getStatusCode().is2xxSuccessful()) {
				return updateWalletResponse;
			}
			
			TradeResponseBankAccountDto responseDto = new TradeResponseBankAccountDto();
			responseDto.setMessage("Uspešno je izvršena razmena " + from + ": " + quantity + " za " + to + ": " + quantityInFiat
					+ ". " + " Stanje bankovnog računa: " );	
			responseDto.setAmountRsd(bankAccount.getAmountRsd());
			responseDto.setAmountEur(bankAccount.getAmountEur());
			responseDto.setAmountUsd(bankAccount.getAmountUsd());
			responseDto.setAmountChf(bankAccount.getAmountChf());
			responseDto.setAmountGbp(bankAccount.getAmountGbp());
		
			
			return ResponseEntity.ok(responseDto);
			
		} else {
			throw new ValidationBrokenException("The entered currency is not valid.");
		}
	}
}
