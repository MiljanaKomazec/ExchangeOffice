package usersService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import usersService.model.UserModel;

public interface UserServiceRepository extends JpaRepository<UserModel, Integer> {
	
	UserModel findByEmail(String email);
	
	@Modifying
	@Transactional
	@Query("update UserModel u set u.password =?2, u.role=?3 where u.email =?1")
	void updateUser(String email, String password, String role);

}
