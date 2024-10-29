package currencyConversion;

import java.math.BigDecimal;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;


import api.dto.CurrencyConversionResponseDto;
import api.dto.CurrencyExchangeDto;
import api.dto.bankAccount.GetSingleBankAccountDto;
import api.dto.bankAccount.UpdateBankAccountDto;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CurrencyExchangeProxy;
import api.services.CurrencyConversionService;
import jakarta.servlet.http.HttpServletRequest;
import util.exceptions.NoDataFoundException;
import util.exceptions.ValidationBrokenException;

@RestController
public class CurrencyConversionServiceImpl implements CurrencyConversionService {

	@Autowired
	private CurrencyExchangeProxy proxy;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
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
		
		ResponseEntity<GetSingleBankAccountDto> bankAccountResponse = bankAccountProxy.getAccount(email);
		if (! bankAccountResponse.getStatusCode().is2xxSuccessful()) {
			throw new NoDataFoundException();
		}
	
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
		
		if (availableAmount < quantity.doubleValue()) {
			throw new ValidationBrokenException("Not enough money");
		}
		
		ResponseEntity<CurrencyExchangeDto> currencyExchangeResponse = proxy.getExchange(from, to);
		if (! currencyExchangeResponse.getStatusCode().is2xxSuccessful()) {
			throw new NoDataFoundException();
		}
		
		CurrencyExchangeDto currencyExchange = currencyExchangeResponse.getBody();
		
		double quantityForUpdate = quantity.doubleValue() * 
				currencyExchange.getExchangeValue().doubleValue();
		
		if (from.equals("RSD")) {
			bankAccount.setAmountRsd(bankAccount.getAmountRsd() - quantity.doubleValue());
		} else if (from.equals("EUR")) {
			bankAccount.setAmountEur(bankAccount.getAmountEur() - quantity.doubleValue());
		} else if (from.equals("USD")) {
			bankAccount.setAmountUsd(bankAccount.getAmountUsd() - quantity.doubleValue());
		} else if (from.equals("CHF")) {
			bankAccount.setAmountChf(bankAccount.getAmountChf() - quantity.doubleValue());
		} else if (from.equals("GBP")) {
			bankAccount.setAmountGbp(bankAccount.getAmountGbp() - quantity.doubleValue());
		}
		
		if (to.equals("RSD")) {
			bankAccount.setAmountRsd(bankAccount.getAmountRsd() + quantityForUpdate);
		} else if (to.equals("EUR")) {
			bankAccount.setAmountEur(bankAccount.getAmountEur() + quantityForUpdate);
		} else if (to.equals("USD")) {
			bankAccount.setAmountUsd(bankAccount.getAmountUsd() + quantityForUpdate);
		} else if (to.equals("CHF")) {
			bankAccount.setAmountChf(bankAccount.getAmountChf() + quantityForUpdate);
		} else if (to.equals("GBP")) {
			bankAccount.setAmountGbp(bankAccount.getAmountGbp() + quantityForUpdate);
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
		
		CurrencyConversionResponseDto responseDto = new CurrencyConversionResponseDto();
		responseDto.setAmountRsd(bankAccount.getAmountRsd());
		responseDto.setAmountEur(bankAccount.getAmountEur());
		responseDto.setAmountUsd(bankAccount.getAmountUsd());
		responseDto.setAmountChf(bankAccount.getAmountChf());
		responseDto.setAmountGbp(bankAccount.getAmountGbp());
		responseDto.setMessage("Uspešno je izvršena razmena " + from + ": " + quantity + " za " + to + ": " + quantityForUpdate);
		
		return ResponseEntity.ok(responseDto);
		
	}
}
