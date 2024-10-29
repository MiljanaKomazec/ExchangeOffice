package bankAccountService.implementation;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.bankAccount.CreateBankAccountDto;
import api.dto.bankAccount.GetSingleBankAccountDto;
import api.dto.bankAccount.UpdateBankAccountDto;
import api.services.BankAccountService;
import bankAccountService.model.BankAccount;
import bankAccountService.repository.BankAccountRepository;
import jakarta.servlet.http.HttpServletRequest;
import util.exceptions.ValidationBrokenException;


@RestController
public class BankAccountServiceImplemetation implements BankAccountService {

	@Autowired
	private BankAccountRepository repository;
	
	@Override
	public List<GetSingleBankAccountDto> getBankAccounts() {
		List<BankAccount> listOfModels = repository.findAll();
		ArrayList<GetSingleBankAccountDto> listOfDtos = new ArrayList<GetSingleBankAccountDto>();
		for(BankAccount um: listOfModels) {
			listOfDtos.add(convertModelToDto(um));
		}
		return listOfDtos;
	}
	
	public ResponseEntity<?> GetAccountById(int id){
		Optional<BankAccount> optBankAccount = repository.findById(id);
		if (optBankAccount.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optBankAccount.get()));
	}
	
	@Override
	public ResponseEntity<?> GetAccountByEmail(String email) {
		Optional<BankAccount> optBankAccount = repository.findByEmail(email);
		if (optBankAccount.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optBankAccount.get()));
	}
	
	@Override
	public ResponseEntity<?> GetMyAccount(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty()) {
			  throw new ValidationBrokenException("You are not allowed to perform this operation.");
		}
		
		String base64UserNameAndPassword = authHeader.split(" ")[1];
		byte[] decodedBytes = Base64.getDecoder().decode(base64UserNameAndPassword);
		String decodedString = new String(decodedBytes);
		String email = decodedString.split(":")[0];
		Optional<BankAccount> optBankAccount = repository.findByEmail(email);
		if (optBankAccount.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optBankAccount.get()));
	}
	
	public ResponseEntity<?> CreateAccount(CreateBankAccountDto dto){
		if(repository.findByEmail(dto.getEmail()).isEmpty()) {
			BankAccount model = convertDtoToModelCreate(dto);
			return ResponseEntity.status(201).body(repository.save(model));
		}
		
		throw new ValidationBrokenException("Forwarded email already exists");
	}

	public ResponseEntity<?> UpdateAccount(UpdateBankAccountDto dto){
		Optional<BankAccount> optBankAccount = repository.findById(dto.getId());
		if (optBankAccount.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		BankAccount model = convertDtoToModelUpdate(dto);
		return ResponseEntity.status(200).body(repository.save(model));
	}
	
	public ResponseEntity<?> DeleteAccount(String email){
		Optional<BankAccount> optBankAccount = repository.findByEmail(email);
		if (optBankAccount.isEmpty()) {
			throw new ValidationBrokenException();
		}
		
		repository.delete(optBankAccount.get());
		return ResponseEntity.status(204).body(null);
	}
	
	@Override
	public ResponseEntity<?> DeleteAccountById(int id) {
		Optional<BankAccount> optBankAccount = repository.findById(id);
		if (optBankAccount.isEmpty()) {
			throw new ValidationBrokenException();
		}
		
		repository.delete(optBankAccount.get());
		return ResponseEntity.status(204).body(null);
	}
	
	public GetSingleBankAccountDto convertModelToDto(BankAccount model) {
		GetSingleBankAccountDto dto = new GetSingleBankAccountDto();
		dto.setId(model.getId());
		dto.setEmail(model.getEmail());
		dto.setAmountEur(model.getAmountEur());
		dto.setAmountRsd(model.getAmountRsd());
		dto.setAmountGbp(model.getAmountGbp());
		dto.setAmountChf(model.getAmountChf());
		dto.setAmountUsd(model.getAmountUsd());
		return dto;
	}
	
	private BankAccount convertDtoToModelCreate(CreateBankAccountDto dto) {
		BankAccount bankAccount = new BankAccount();
		
		bankAccount.setEmail(dto.getEmail());
		bankAccount.setAmountEur(dto.getAmountEur());
		bankAccount.setAmountRsd(dto.getAmountRsd());
		bankAccount.setAmountGbp(dto.getAmountGbp());
		bankAccount.setAmountChf(dto.getAmountChf());
		bankAccount.setAmountUsd(dto.getAmountUsd());
		return bankAccount;
	}
	
	private BankAccount convertDtoToModelUpdate(UpdateBankAccountDto dto) {
		BankAccount bankAccount = new BankAccount();
		
		bankAccount.setId(dto.getId());
		bankAccount.setEmail(dto.getEmail());
		bankAccount.setAmountEur(dto.getAmountEur());
		bankAccount.setAmountRsd(dto.getAmountRsd());
		bankAccount.setAmountGbp(dto.getAmountGbp());
		bankAccount.setAmountChf(dto.getAmountChf());
		bankAccount.setAmountUsd(dto.getAmountUsd());
		return bankAccount;
	}
}
