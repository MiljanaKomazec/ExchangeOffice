package api.feignProxies;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import api.dto.bankAccount.CreateBankAccountDto;
import api.dto.bankAccount.GetSingleBankAccountDto;
import api.dto.bankAccount.UpdateBankAccountDto;

@FeignClient("bank-account-service/bank-account")
public interface BankAccountProxy {
	
	@PostMapping
	ResponseEntity<?> createAccount(@RequestBody CreateBankAccountDto dto);
	
	@DeleteMapping
	ResponseEntity<?> deleteAccount(@RequestBody String email);
	
	@GetMapping("/email")
	ResponseEntity<GetSingleBankAccountDto> getAccount(@RequestParam("email") String email);
	
	@PutMapping
	ResponseEntity<?> updateAccount(@RequestBody UpdateBankAccountDto dto);
}
