package cryptoWalletService.implementation;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.cryptoWallet.CreateCryptoWallet;
import api.dto.cryptoWallet.GetSingleCryptoWallet;
import api.dto.cryptoWallet.UpdateCryptoWallet;
import api.services.CryptoWalletService;
import cryptoWalletService.model.CryptoWalletModel;
import cryptoWalletService.repository.CryptoWalletRepository;
import jakarta.servlet.http.HttpServletRequest;
import util.exceptions.ValidationBrokenException;

@RestController
public class CryptoWalletImpl implements CryptoWalletService {

	
	@Autowired
	private CryptoWalletRepository repository;
	
	@Override
	public List<GetSingleCryptoWallet> getCryptoWallets() {
		List<CryptoWalletModel> listOfModels = repository.findAll();
		ArrayList<GetSingleCryptoWallet> listOfDtos = new ArrayList<GetSingleCryptoWallet>();
		for(CryptoWalletModel um: listOfModels) {
			listOfDtos.add(convertModelToDto(um));
		}
		return listOfDtos;
	}

	
	@Override
	public ResponseEntity<?> GetWaletById(int id) {
		Optional<CryptoWalletModel> optWallet = repository.findById(id);
		if (optWallet.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optWallet.get()));
	}
	

	@Override
	public ResponseEntity<?> GetWaletByEmail(String email) {
		Optional<CryptoWalletModel> optWallet = repository.findByEmail(email);
		if (optWallet.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optWallet.get()));
	}
	
	@Override
	public ResponseEntity<?> GetMyWalet(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty()) {
			  throw new ValidationBrokenException("You are not allowed to perform this operation.");
		}
		
		String base64UserNameAndPassword = authHeader.split(" ")[1];
		byte[] decodedBytes = Base64.getDecoder().decode(base64UserNameAndPassword);
		String decodedString = new String(decodedBytes);
		String email = decodedString.split(":")[0];
		Optional<CryptoWalletModel> optWallet = repository.findByEmail(email);
		if (optWallet.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.ok(convertModelToDto(optWallet.get()));
	}

	@Override
	public ResponseEntity<?> CreateWalet(CreateCryptoWallet dto) {
		if(repository.findByEmail(dto.getEmail()).isEmpty()) {
			CryptoWalletModel model = convertDtoToModelCreate(dto);
			return ResponseEntity.status(201).body(repository.save(model));
		}
		
		throw new ValidationBrokenException("Forwarded email already exists");
	}

	@Override
	public ResponseEntity<?> UpdateWalet(UpdateCryptoWallet dto) {
		Optional<CryptoWalletModel> optWallet = repository.findById(dto.getId());
		if (optWallet.isEmpty()) {
			ResponseEntity.badRequest().build();
		}
		
		CryptoWalletModel model = convertDtoToModelUpdate(dto);
		return ResponseEntity.status(200).body(repository.save(model));
	}

	@Override
	public ResponseEntity<?> DeleteWalet(String email) {
		Optional<CryptoWalletModel> optWallet = repository.findByEmail(email);
		if (optWallet.isEmpty()) {
			throw new ValidationBrokenException();
		}
		
		repository.delete(optWallet.get());
		return ResponseEntity.status(204).body(null);
	}

	@Override
	public ResponseEntity<?> DeleteWaletById(int id) {
		Optional<CryptoWalletModel> optWallet = repository.findById(id);
		if (optWallet.isEmpty()) {
			throw new ValidationBrokenException();
		}
		
		repository.delete(optWallet.get());
		return ResponseEntity.status(204).body(null);
	}
	
	public GetSingleCryptoWallet convertModelToDto(CryptoWalletModel model) {
		GetSingleCryptoWallet dto = new GetSingleCryptoWallet();
		dto.setId(model.getId());
		dto.setEmail(model.getEmail());
		dto.setAmountBTC(model.getAmountBTC());
		dto.setAmountLTC(model.getAmountLTC());
		dto.setAmountXMR(model.getAmountXMR());
		return dto;
	}
	
	private CryptoWalletModel convertDtoToModelCreate(CreateCryptoWallet dto) {
		CryptoWalletModel cryptoWallet = new CryptoWalletModel();
		
		cryptoWallet.setEmail(dto.getEmail());
		cryptoWallet.setEmail(dto.getEmail());
		cryptoWallet.setAmountBTC(dto.getAmountBTC());
		cryptoWallet.setAmountLTC(dto.getAmountLTC());
		cryptoWallet.setAmountXMR(dto.getAmountXMR());
		return cryptoWallet;
	}
	
	private CryptoWalletModel convertDtoToModelUpdate(UpdateCryptoWallet dto) {
		CryptoWalletModel cryptoWallet = new CryptoWalletModel();
		
		cryptoWallet.setId(dto.getId());
		cryptoWallet.setEmail(dto.getEmail());
		cryptoWallet.setEmail(dto.getEmail());
		cryptoWallet.setAmountBTC(dto.getAmountBTC());
		cryptoWallet.setAmountLTC(dto.getAmountLTC());
		cryptoWallet.setAmountXMR(dto.getAmountXMR());
		return cryptoWallet;
	}

}
