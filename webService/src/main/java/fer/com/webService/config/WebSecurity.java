package fer.com.webService.config;

import fer.com.webService.model.User;
import fer.com.webService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class WebSecurity {

    @Autowired
    private UserRepository repo;

    public boolean checkUserId(Authentication authentication, int id) {
        String name = authentication.getName();
        User user = repo.findByUsername(name);
        return user != null && user.getId() == id;
    }
}
