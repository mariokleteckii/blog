package fer.com.webService.services;

import fer.com.webService.dto.UserRegistrationDto;
import fer.com.webService.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Iterable<User> getAllUsers();
    Optional<User> save(User user, Long id);
    User getUserById(Long id);
    User saveUser(UserRegistrationDto registrationDto);

}
