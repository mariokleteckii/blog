package fer.com.webService.repositories;

import fer.com.webService.model.User;
import org.springframework.data.repository.CrudRepository;


public interface UserDetailsRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String username);

}