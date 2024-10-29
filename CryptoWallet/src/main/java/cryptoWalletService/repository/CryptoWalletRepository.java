package cryptoWalletService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cryptoWalletService.model.CryptoWalletModel;


public interface CryptoWalletRepository extends JpaRepository<CryptoWalletModel, Integer> {
	Optional<CryptoWalletModel> findByEmail(String email);

}
