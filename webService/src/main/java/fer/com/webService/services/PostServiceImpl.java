package fer.com.webService.services;

import fer.com.webService.model.Post;
import fer.com.webService.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Override
    public Iterable<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public String addPost(Post post) {
        if(postRepository.save(post) != null)
            return "Saved successfully!!";
        else
            return "Photo save unsuccessful!!";
    }


    @Override
    public Optional<Post> save(Post post, Long id) {
        return postRepository.findById(id).map(repoPhoto -> {
            repoPhoto.setText(post.getText());
            return postRepository.save(repoPhoto);
        });

    }

}
