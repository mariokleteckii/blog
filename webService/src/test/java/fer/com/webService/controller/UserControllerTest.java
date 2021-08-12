package fer.com.webService.controller;

import fer.com.webService.model.Post;
import fer.com.webService.model.Role;
import fer.com.webService.model.User;
import fer.com.webService.repositories.UserRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@WebAppConfiguration
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(value = "test")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {

    @Autowired
    private WebApplicationContext context;


    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private MockMvc mockMvc;


    Post mockPost1 = new Post((long) 1, "Novi post");
    Post mockPost2 = new Post((long) 2, "Novi post2");

    List<Post> listOfPosts = Arrays.asList(mockPost1, mockPost2);

    Role mockRole = new Role((long) 1, "USER");

    @Mock
    User mockUser = new User((long) 1, "User1", "password", Arrays.asList(mockRole), listOfPosts);



    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void getAllUsers() throws Exception{

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andReturn();

    }

    @Test
    public void updateUser() throws Exception{

        Optional<User> optionalUser = Optional.ofNullable(mockUser);
        when(userService.save(Mockito.any(User.class), Mockito.anyLong())).thenReturn(optionalUser);

        MvcResult result = mockMvc.perform(put("/users/{userId}", 1)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content("{\"id\":\"2\", \"username\":\"User2\", \"password\":\"pass\"}")
                .with(csrf())
                .with(user("user").password("pass").roles("USER")))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void deletePostFromUser() throws Exception{

        ResultMatcher ok = MockMvcResultMatchers.status().isOk();
        Optional<User> optionalUser = Optional.ofNullable(mockUser);

        when(userRepository.findById(Mockito.anyLong())).thenReturn(optionalUser);

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.delete("/users/1")
                .with(csrf())
                .with(user("user").password("pass").roles("USER"));

        this.mockMvc.perform(builder)
                .andDo(print())
                .andExpect(ok)
                .andReturn();

    }


}
