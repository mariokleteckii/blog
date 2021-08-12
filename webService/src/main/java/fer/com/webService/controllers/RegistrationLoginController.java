package fer.com.webService.controllers;

import fer.com.webService.dto.UserRegistrationDto;
import fer.com.webService.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationLoginController {

    private UserService userService;

    public RegistrationLoginController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto(){
        return new UserRegistrationDto();
    }

    @GetMapping("/registration")
    public String showRegistrationForm(){
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") UserRegistrationDto userRegistrationDto){
        userService.saveUser(userRegistrationDto);
        return "redirect:/registration?success";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

}
