package fer.com.webService.services;

import fer.com.webService.model.Post;

import java.util.Optional;

public interface PostService {

    Iterable<Post> getAllPosts();
    String addPost(Post post);
    Optional<Post> save(Post post, Long id);

}
