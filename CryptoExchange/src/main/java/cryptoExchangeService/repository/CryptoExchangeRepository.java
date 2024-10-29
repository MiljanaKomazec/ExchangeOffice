package cryptoExchangeService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cryptoExchangeService.model.CryptoExchangeModel;

public interface CryptoExchangeRepository extends JpaRepository<CryptoExchangeModel, Integer> {
	CryptoExchangeModel findByFromAndTo(String from, String to);
}
