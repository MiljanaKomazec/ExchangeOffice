package usersService.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.UserDto;
import api.dto.bankAccount.CreateBankAccountDto;
import api.dto.cryptoWallet.CreateCryptoWallet;
import api.feignProxies.BankAccountProxy;
import api.feignProxies.CryptoWalletProxy;
import api.services.UsersService;
import usersService.model.UserModel;
import usersService.repository.UserServiceRepository;
import util.exceptions.ValidationBrokenException;

@RestController
public class UserServiceImplementation implements UsersService {

	@Autowired
	private UserServiceRepository repo;
	
	@Autowired
	private BankAccountProxy bankAccountProxy;
	
	@Autowired
	private CryptoWalletProxy cryptoWalletProxy;

	@Override
	public List<UserDto> getUsers() {
		List<UserModel> listOfModels = repo.findAll();
		ArrayList<UserDto> listOfDtos = new ArrayList<UserDto>();
		for(UserModel um: listOfModels) {
			listOfDtos.add(convertModelToDto(um));
		}
		return listOfDtos;
 	}

	@Override
	public ResponseEntity<?> createAdmin(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("ADMIN");
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		return ResponseEntity.status(409).body("Admin with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> createUser(UserDto dto) throws Exception {
		if(repo.findByEmail(dto.getEmail()) == null) {
			dto.setRole("USER");
			
			CreateBankAccountDto bankDto = new CreateBankAccountDto();
			bankDto.setEmail(dto.getEmail());
			ResponseEntity<?> responseFromBankService = bankAccountProxy.createAccount(bankDto);
			
			CreateCryptoWallet cryptoWalletDto = new CreateCryptoWallet();
			cryptoWalletDto.setEmail(dto.getEmail());
			ResponseEntity<?> responseFromCryptoWalletService = cryptoWalletProxy.createWallet(cryptoWalletDto);
			
			if (responseFromBankService.getStatusCode().is4xxClientError()) {
				return responseFromBankService;
			} else if (!responseFromBankService.getStatusCode().is2xxSuccessful()) {
				throw new Exception();
			} else if (responseFromCryptoWalletService.getStatusCode().is4xxClientError()) {
				return responseFromCryptoWalletService;
			} else if (!responseFromCryptoWalletService.getStatusCode().is2xxSuccessful()) {
				throw new Exception();
			}
			
			UserModel model = convertDtoToModel(dto);
			return ResponseEntity.status(201).body(repo.save(model));
		}
		return ResponseEntity.status(409).body("User with forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> updateUser(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) != null) {
			UserModel optUser = repo.findByEmail(dto.getEmail());
			System.out.println(optUser.getRole());
			if(optUser.getRole().equals("USER")) {
				repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
				return ResponseEntity.status(201).body(dto);
			} else {
				throw new ValidationBrokenException("Admin can only update users with the user role.");
			}
			
		}
		return ResponseEntity.status(404).body("User with forwarded email does not exists");
	}
	
	@Override
	public ResponseEntity<?> updateAdmin(UserDto dto) {
		if(repo.findByEmail(dto.getEmail()) != null) {
			repo.updateUser(dto.getEmail(), dto.getPassword(), dto.getRole());
			return ResponseEntity.status(201).body(dto);
		}
		return ResponseEntity.status(404).body("User with forwarded email does not exists");
	}
	
	@Override
	public ResponseEntity<?> deleteUser(int id) throws Exception {
		Optional<UserModel> optUser = repo.findById(id);
		if (optUser.isEmpty()) {
			throw new ValidationBrokenException("User doesn't exist");
		}
		
		ResponseEntity<?> responseFromBankService = bankAccountProxy.deleteAccount(optUser.get().getEmail());
		ResponseEntity<?> responseFromCryptoWallerService = cryptoWalletProxy.deleteWallet(optUser.get().getEmail());
		if (responseFromBankService.getStatusCode().is4xxClientError()) {
			return responseFromBankService;
		} else if (!responseFromBankService.getStatusCode().is2xxSuccessful()) {
			throw new Exception();
		} else if (responseFromBankService.getStatusCode().is4xxClientError()) {
			return responseFromCryptoWallerService;
		} else if (!responseFromCryptoWallerService.getStatusCode().is2xxSuccessful()) {
			throw new Exception();
		}
		
		repo.delete(optUser.get());
		return ResponseEntity.status(204).body(null);
	}
	
	public UserModel convertDtoToModel(UserDto dto) {
		return new UserModel(dto.getEmail(), dto.getPassword(), dto.getRole());
	}
	
	public UserDto convertModelToDto(UserModel model) {
		return new UserDto(model.getEmail(), model.getPassword(), model.getRole());
	}
}
