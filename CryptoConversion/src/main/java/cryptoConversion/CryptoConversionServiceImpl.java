package cryptoConversion;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import api.dto.CryptoConversionResponseDto;
import api.dto.CryptoExchangeDto;
import api.dto.CurrencyConversionDto;
import api.dto.CurrencyExchangeDto;
import api.dto.cryptoWallet.GetSingleCryptoWallet;
import api.dto.cryptoWallet.UpdateCryptoWallet;
import api.feignProxies.CryptoExchangeProxy;
import api.feignProxies.CryptoWalletProxy;
import api.feignProxies.CurrencyExchangeProxy;
import api.services.CryptoConversionService;
import api.services.CurrencyConversionService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import jakarta.servlet.http.HttpServletRequest;
import util.exceptions.NoDataFoundException;
import util.exceptions.ValidationBrokenException;

@RestController
public class CryptoConversionServiceImpl implements CryptoConversionService {

	@Autowired
	private CryptoExchangeProxy cryptoExchangeProxy;
	
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
		
		if (availableAmount < quantity.doubleValue()) {
			throw new ValidationBrokenException("Not enough money");
		}
		
		ResponseEntity<CryptoExchangeDto> cryptoExchangeResponse = cryptoExchangeProxy.getExchange(from, to);
		if (! cryptoExchangeResponse.getStatusCode().is2xxSuccessful()) {
			throw new NoDataFoundException();
		}
		
		CryptoExchangeDto cryptoExchange = cryptoExchangeResponse.getBody();
		
		double quantityForUpdate = quantity.doubleValue() * 
				cryptoExchange.getExchangeValue().doubleValue();
		
		if (from.equals("BTC")) {
			cryptoWallet.setAmountBTC(cryptoWallet.getAmountBTC() - quantity.doubleValue());
		} else if (from.equals("LTC")) {
			cryptoWallet.setAmountLTC(cryptoWallet.getAmountLTC() - quantity.doubleValue());
		} else if (from.equals("XMR")) {
			cryptoWallet.setAmountXMR(cryptoWallet.getAmountXMR() - quantity.doubleValue());
		}
		
		if (to.equals("BTC")) {
			cryptoWallet.setAmountBTC(cryptoWallet.getAmountBTC() + quantityForUpdate);
		} else if (to.equals("LTC")) {
			cryptoWallet.setAmountLTC(cryptoWallet.getAmountLTC() + quantityForUpdate);
		} else if (to.equals("XMR")) {
			cryptoWallet.setAmountXMR(cryptoWallet.getAmountXMR() + quantityForUpdate);
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
		
		CryptoConversionResponseDto responseDto = new CryptoConversionResponseDto();
		responseDto.setAmountBTC(cryptoWallet.getAmountBTC());
		responseDto.setAmountLTC(cryptoWallet.getAmountLTC());
		responseDto.setAmountXMR(cryptoWallet.getAmountXMR());
		responseDto.setMessage("Uspešno je izvršena razmena " + from + ": " + quantity + " za " + to + ": " + quantityForUpdate);
		
		return ResponseEntity.ok(responseDto);
		
	}
}
