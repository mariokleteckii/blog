package fer.com.webService.repositories;

import fer.com.webService.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserById(Long id);
    User findByUsername(String username);

}
