package idu.cs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import idu.cs.domain.User;

public interface UserRepository 
	extends JpaRepository<User, Long> {
	List<User> findByName(String name);
	// name은 유일키가 아니므로 list로 해줌
	List<User> findByCompany(String company);
	User findByUserId(String userId); //ByUserId == where userid=''
}
