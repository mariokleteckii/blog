package fer.com.webService.controller;

import fer.com.webService.model.Post;
import fer.com.webService.model.Role;
import fer.com.webService.model.User;
import fer.com.webService.repositories.PostRepository;
import fer.com.webService.repositories.UserRepository;
import fer.com.webService.services.PostService;
import fer.com.webService.services.UserService;
import fer.com.webService.test.TestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "test")
@RunWith(SpringJUnit4ClassRunner.class)
public class PostControllerTest {

    @Autowired
    private WebApplicationContext context;


    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @MockBean
    private PostService postService;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PostRepository postRepository;

    Post mockPost1 = new Post((long) 1, "Novi post");
    Post mockPost2 = new Post((long) 2, "Novi post2");

    List<Post> listOfPosts = Arrays.asList(mockPost1, mockPost2);

    Role mockRole = new Role((long) 1, "USER");

    @Mock
    User mockUser = new User((long) 1, "User1", "password", Arrays.asList(mockRole), listOfPosts);


    @Test
    public void getAllPosts() throws Exception{

        when(postService.getAllPosts()).thenReturn(Arrays.asList(mockPost1));//,mockPost2,mockPost3));

        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .with(user("user").password("pass").roles("USER")))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].text").value("Novi post"))
                .andReturn();

    }

    @Test
    public void getAllPostsFromUser() throws Exception{
        when(userService.getUserById(Mockito.anyLong())).thenReturn(mockUser);
        when(mockUser.getPosts()).thenReturn(listOfPosts);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/posts", 1)
                .with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andReturn();

    }

    @Test
    public void addPost() throws Exception{
        when(userRepository.findUserById(Mockito.anyLong())).thenReturn(mockUser);

        when(postService.addPost(Mockito.any(Post.class))).thenReturn("Saved");

        mockMvc.perform(post("/users/{userId}/posts", 1)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(mockPost1))
                .with(csrf())
                .with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

    }

    @Test
    public void updatePost() throws Exception {

        Optional<Post> optionalPost = Optional.ofNullable(mockPost2);
        when(postService.save(Mockito.any(Post.class), Mockito.anyLong())).thenReturn(optionalPost);

        MvcResult result = mockMvc.perform(put("/users/{userId}/posts/{postId}", 1, 1)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content("{\"id\":\"2\", \"text\":\"Update posta\"}")
                .with(csrf())
                .with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();


    }

    @Test
    public void deletePostFromUser() throws Exception{

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        when(userService.getUserById(Mockito.anyLong())).thenReturn(mockUser);
        when(mockUser.getPosts()).thenReturn(listOfPosts);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/users/1/posts/1")
                .with(csrf())
                .with(user("user").password("pass").roles("USER"));

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(ok)
                .andReturn();

    }

}


