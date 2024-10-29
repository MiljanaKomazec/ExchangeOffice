package cryptoExchangeService.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import api.dto.CryptoExchangeDto;
import api.services.CryptoExchangeService;
import cryptoExchangeService.model.CryptoExchangeModel;
import cryptoExchangeService.repository.CryptoExchangeRepository;

@RestController
public class CryptoExchangeImpl implements CryptoExchangeService {
	
	@Autowired
	private CryptoExchangeRepository repo;
	
	@Autowired
	private Environment environment;
	
	
	public CryptoExchangeDto convertFromModelToDto (CryptoExchangeModel model) {
		CryptoExchangeDto dto = new CryptoExchangeDto(model.getFrom(), model.getTo(), model.getExchangeValue());
		dto.setInstancePort(environment.getProperty("local.server.port"));
		return dto;
	}


	@Override
	public ResponseEntity<?> getExchange(String from, String to) {
		CryptoExchangeModel model = repo.findByFromAndTo(from, to);
		if(model == null) {
			return ResponseEntity.status(404).body("Requested exchange pair [" + from
					+ " into " + to + "] could not be found.");
		}
		return ResponseEntity.ok(convertFromModelToDto(model));
	}

}
