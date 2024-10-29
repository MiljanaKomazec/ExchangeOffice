package api.feignProxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dto.cryptoWallet.CreateCryptoWallet;
import api.dto.cryptoWallet.GetSingleCryptoWallet;
import api.dto.cryptoWallet.UpdateCryptoWallet;

@FeignClient("crypto-wallet-service/crypto-wallet")
public interface CryptoWalletProxy {
	@GetMapping("/email")
	ResponseEntity<GetSingleCryptoWallet> getWallet(@RequestParam("email") String email);
	
	@PostMapping
	ResponseEntity<?> createWallet(@RequestBody CreateCryptoWallet dto);
	
	@PutMapping
	ResponseEntity<?> updateWalet(@RequestBody UpdateCryptoWallet dto);
	
	@DeleteMapping
	ResponseEntity<?> deleteWallet(@RequestBody String email);
}
