package com.example.taskmanager;

import com.example.taskmanager.entity.*;
import com.example.taskmanager.repo.CommentRepository;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.json.JSONObject;
import org.testcontainers.utility.DockerImageName;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import java.util.List;
import java.util.Set;
import com.redis.testcontainers.RedisContainer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractTest {

    String token;

    @Container
    @ServiceConnection
    protected static PostgreSQLContainer postgreSQLContainer;

    static {
        DockerImageName postgres = DockerImageName.parse("postgres:12.3");
        postgreSQLContainer = (PostgreSQLContainer) new PostgreSQLContainer(postgres)
                .withReuse(true);
        postgreSQLContainer.start();
    }

    static {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("redis:5.0.3-alpine")).withExposedPorts(6379);
        redis.start();
        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

//    @Container
//    @ServiceConnection
//    static RedisContainer redisContainer = new RedisContainer(DockerImageName.parse("7.0.12"));

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();

        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);
    }

    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected PasswordEncoder passwordEncoder;
    @Autowired
    protected ObjectMapper objectMapper;


    @BeforeEach
    public void setup() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN,
                "$2a$10$/UVYzqD6YUWKCDQ/SB/AtuO7vvjKN9L3j7mGG3RH2Co.pFZmpNLES");
        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER,
                "$2a$10$uN9oAjVnj/m2EPrvRIL6/ef68XYThJjeniAE8jurdWaGvMbfCemjG");

//        Task task1 = createTask("Title", "desc", Status.WAITING, Priority.HIGH, admin, user, null);

        ResultActions perform = mockMvc.perform(post("/api/v1/auth/signin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"email\": \"admin@mail.com\", \"password\": \"admin\" }")
                .accept(MediaType.APPLICATION_JSON));
        MvcResult mvcResult = perform.andDo(print()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        JSONObject json = new JSONObject(contentAsString);
        token = "Bearer " + json.getString("token");
        System.out.println(token);
    }

    @AfterEach
    public void afterEach() {
        commentRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();

    }

    protected User createUser(String username, String email, RoleType role, String password) {
        return userRepository.save(User.builder()
                .username(username)
                .email(email)
                .roles(Set.of(role))
                .password(password)
                .build());
    }

    protected Task createTask(String title, String description, Status status, Priority priority,
                              User author, User executor, List<Comment> comments){
        return taskRepository.save(Task.builder()
                .title(title)
                .description(description)
                .status(status)
                .priority(priority)
                .author(author)
                .executor(executor)
                .comments(comments)
                .build());

    }

    protected Comment createComment(String comment, User author, Task task){
        return commentRepository.save(Comment.builder()
                        .comment(comment)
                        .author(author)
                        .task(task)
                .build());
    }
}





