package bankAccountService.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import bankAccountService.model.BankAccount;

public interface BankAccountRepository extends JpaRepository<BankAccount, Integer> {
	Optional<BankAccount> findByEmail(String email);
}
