package idu.cs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import idu.cs.domain.User;

@Repository   //이 인터페이스는 repository라고 지정, spring에게 알려줌
public interface UserRepository 
	extends JpaRepository<User, Long> {
	List<User> findByName(String name);
	// name은 유일키가 아니므로 list로 해줌
	List<User> findByCompany(String company);
	User findByUserId(String userId); //ByUserId == where userid=''
}
