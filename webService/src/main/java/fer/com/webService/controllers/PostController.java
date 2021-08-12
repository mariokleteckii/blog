package fer.com.webService.controllers;

import fer.com.webService.exceptions.NotFoundException;
import fer.com.webService.model.Post;
import fer.com.webService.model.User;
import fer.com.webService.repositories.PostRepository;
import fer.com.webService.repositories.UserRepository;
import fer.com.webService.services.PostService;
import fer.com.webService.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {

    private PostService postService;
    private PostRepository postRepository;
    private UserService userService;
    private UserRepository userRepository;

    public PostController(PostService postService, PostRepository postRepository, UserService userService, UserRepository userRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/posts")
    public Iterable<Post> getAllPosts(){
        return postService.getAllPosts();
    }


    @PostMapping("/users/{userId}/posts")
    public String addPost(@RequestBody Post post, @PathVariable Long userId){
        User user = userRepository.findUserById(userId);
        user.getPosts().add(post);
        return postService.addPost(post);
    }

    @PutMapping("/users/{userId}/posts/{postId}")
    public ResponseEntity<Post> updatePost(@RequestBody Post post, @PathVariable Long userId, @PathVariable Long postId){
        if(!(postService.save(post, postId)).isPresent())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/users/{userId}/posts")
    public ModelAndView getAllPostsFromUser(@PathVariable Long userId, Model model){
        User user = userService.getUserById(userId);
        model.addAttribute("posts", user.getPosts());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("posts");
        return modelAndView;
    }

    @DeleteMapping("/users/{userId}/posts/{postId}")
    public void deletePostFromUser(@PathVariable Long userId, @PathVariable Long postId){
        User user = userService.getUserById(userId);
        boolean k = false;
        List<Post> newList = new ArrayList<>();

        if(user.getPosts() != null) {
            for(Post post:user.getPosts()){
                if(post.getId() == postId){
                    k = true;
                }
                else {
                    newList.add(post);
                }
            }
            if(k) {
                user.setPosts(newList);
                postRepository.deleteById(postId);
            }
            if(!k){
                throw new NotFoundException();
            }
        }
        else {
            throw new NotFoundException();
        }
    }

}
