package api.services;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import api.dto.cryptoWallet.CreateCryptoWallet;
import api.dto.cryptoWallet.GetSingleCryptoWallet;
import api.dto.cryptoWallet.UpdateCryptoWallet;
import jakarta.servlet.http.HttpServletRequest;

public interface CryptoWalletService {
	
	@GetMapping("/crypto-wallet")
	List<GetSingleCryptoWallet> getCryptoWallets();
	
	@GetMapping("/crypto-wallet/{id}")
	public ResponseEntity<?> GetWaletById(@PathVariable int id);
	
	@GetMapping("/crypto-wallet/email")
	public ResponseEntity<?> GetWaletByEmail(@RequestParam("email") String email);
	
	@PostMapping("/crypto-wallet")
	public ResponseEntity<?> CreateWalet(@RequestBody CreateCryptoWallet dto);
	
	@PutMapping("/crypto-wallet")
	public ResponseEntity<?> UpdateWalet(@RequestBody UpdateCryptoWallet dto);
	
	@DeleteMapping("/crypto-wallet")
	public ResponseEntity<?> DeleteWalet(@RequestBody String email);
	
	@DeleteMapping("/crypto-wallet/{id}")
	public ResponseEntity<?> DeleteWaletById(@PathVariable int id);
	
	@GetMapping("/crypto-wallet/my-wallet")
	public ResponseEntity<?> GetMyWalet(HttpServletRequest request);
}
