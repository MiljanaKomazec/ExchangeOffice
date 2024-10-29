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

import api.dto.bankAccount.CreateBankAccountDto;
import api.dto.bankAccount.GetSingleBankAccountDto;
import api.dto.bankAccount.UpdateBankAccountDto;
import jakarta.servlet.http.HttpServletRequest;

public interface BankAccountService {

	@GetMapping("/bank-account")
	List<GetSingleBankAccountDto> getBankAccounts();
	
	@GetMapping("/bank-account/{id}")
	public ResponseEntity<?> GetAccountById(@PathVariable int id);
	
	@GetMapping("/bank-account/email")
	public ResponseEntity<?> GetAccountByEmail(@RequestParam("email") String email);
	
	@PostMapping("/bank-account")
	public ResponseEntity<?> CreateAccount(@RequestBody CreateBankAccountDto dto);
	
	@PutMapping("/bank-account")
	public ResponseEntity<?> UpdateAccount(@RequestBody UpdateBankAccountDto dto);
	
	@DeleteMapping("/bank-account")
	public ResponseEntity<?> DeleteAccount(@RequestBody String email);
	
	@DeleteMapping("/bank-account/{id}")
	public ResponseEntity<?> DeleteAccountById(@PathVariable int id);
	
	@GetMapping("/bank-account/my-account")
	public ResponseEntity<?> GetMyAccount(HttpServletRequest request);
}
