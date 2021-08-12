package fer.com.webService.controllers;

import fer.com.webService.exceptions.NotFoundException;
import fer.com.webService.model.User;
import fer.com.webService.repositories.UserRepository;
import fer.com.webService.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class UserController {

    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository){
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    @ResponseBody()
    public String home(){
        return "Documentation";
    }


    @GetMapping("/users")
    public ModelAndView getAllUsers(Model model){
        model.addAttribute("users", userService.getAllUsers());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("users");
        return modelAndView;
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable Long id){
        if(!(userService.save(user, id)).isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id){

        if(!(userRepository.findById(id)).isPresent())
            throw new NotFoundException("Ne postoji user s id-om:" + id);

        userRepository.deleteById(id);
    }

}
