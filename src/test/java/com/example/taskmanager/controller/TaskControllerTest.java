package com.example.taskmanager.controller;

import com.example.taskmanager.AbstractTest;
import com.example.taskmanager.dto.mapper.TaskMapper;
import com.example.taskmanager.dto.task.TaskFilter;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.entity.*;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.service.RefreshTokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Integration tests for Task API endpoints")
class TaskControllerTest extends AbstractTest {


    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("Find task by incorrect id, then return founded task with comments")
    public void whenFindById_thenReturnTaskWithComments() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task1 = createTask("Task1", "description task1", Status.WAITING, Priority.MEDIUM, admin, admin, null);
        Comment comment1 = createComment("Comment1", admin, task1);
        Comment comment2 = createComment("Comment2", user, task1);
        task1.addComment(comment1);
        task1.addComment(comment2);


        this.mockMvc.perform(get("/api/v1/task/" + task1.getId())
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.authorId").value(admin.getId()))
                .andExpect(jsonPath("$.executorId").value(admin.getId()))
                .andExpect(jsonPath("$.comments[0].id").value(comment1.getId()))
                .andExpect(jsonPath("$.comments[0].comment").value("Comment1"))
                .andExpect(jsonPath("$.comments[0].authorId").value(admin.getId()))
                .andExpect(jsonPath("$.comments[0].taskId").value(task1.getId()))
                .andExpect(jsonPath("$.comments[1].id").value(comment2.getId()))
                .andExpect(jsonPath("$.comments[1].comment").value("Comment2"))
                .andExpect(jsonPath("$.comments[1].authorId").value(user.getId()))
                .andExpect(jsonPath("$.comments[1].taskId").value(task1.getId()));

    }

    @Test
    @DisplayName("Find task by incorrect id , then return error message")
    public void whenFindByBadId_thenReturnErrorMessage() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");


        mockMvc.perform(get("/api/v1/task/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Task with id 1 not found"));
    }

    @Test
    @DisplayName("Create new task, then return created task")
    public void whenCreateNewTask_thenReturnThisTaskFromDB() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task newTask = Task.builder()
                .title("Title new task")
                .description("This is description new task")
                .status(Status.WAITING)
                .priority(Priority.MEDIUM)
                .author(admin)
                .executor(user)
                .comments(List.of())
                .build();
        UpsertTaskRq rq = taskMapper.taskToUpsertTaskRq(newTask);

        mockMvc.perform(post("/api/v1/task")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.title").value("Title new task"))
                .andExpect(jsonPath("$.description").value("This is description new task"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.priority").value("MEDIUM"))
                .andExpect(jsonPath("$.authorId").isNotEmpty())
                .andExpect(jsonPath("$.executorId").isNotEmpty());

    }


    @Test
    @DisplayName("Edit task author, then return edited task")
    public void whenEditTaskByAuthor_thenReturnEditedTask() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task = createTask("Task1", "description task1", Status.WAITING, Priority.MEDIUM, admin, user, null);
        Comment comment1 = createComment("Comment1", admin, task);
        Comment comment2 = createComment("Comment2", user, task);
        task.addComment(comment1);
        task.addComment(comment2);

        UpsertTaskRq rq = taskMapper.taskToUpsertTaskRq(task);
        rq.setPriority(Priority.HIGH);

        mockMvc.perform(put("/api/v1/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Task1"))
                .andExpect(jsonPath("$.description").value("description task1"))
                .andExpect(jsonPath("$.status").value("WAITING"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.authorId").value(task.getAuthor().getId()))
                .andExpect(jsonPath("$.executorId").value(task.getExecutor().getId()));

    }

    @Test
    @DisplayName("Edit task non author, then return error message")
    public void whenEditTaskNonAuthor_thenReturnErrorMessage() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task = createTask("Task1Up", "description task1up", Status.WAITING, Priority.MEDIUM, admin, user, null);
        Comment comment1 = createComment("Comment1", admin, task);
        Comment comment2 = createComment("Comment2", user, task);
        task.addComment(comment1);
        task.addComment(comment2);

        UpsertTaskRq rq = taskMapper.taskToUpsertTaskRq(task);
        var fakeToken = tokenAdmin + "1";


        mockMvc.perform(put("/api/v1/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, fakeToken)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"));
    }

    @Test
    @DisplayName("Edit status of task author, then return edited task")
    public void whenEditStatusTaskAuthor_thenReturnEditedTask() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task = createTask("Task1Up", "description task1up", Status.WAITING, Priority.MEDIUM, admin, admin, null);
        Comment comment1 = createComment("Comment1", admin, task);
        Comment comment2 = createComment("Comment2", user, task);
        task.addComment(comment1);
        task.addComment(comment2);

        UpsertTaskRq rq = UpsertTaskRq.builder()
                .status(Status.FINISHED)
                .build();


        mockMvc.perform(put("/api/v1/task/status/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    @DisplayName("Edit status of task performer, then return edited task")
    public void whenEditStatusTaskExecutor_thenReturnEditedTask() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task = createTask("Task1Up", "description task1up", Status.WAITING, Priority.MEDIUM, admin, user, null);
        Comment comment1 = createComment("Comment1", admin, task);
        Comment comment2 = createComment("Comment2", user, task);
        task.addComment(comment1);
        task.addComment(comment2);

        UpsertTaskRq rq = UpsertTaskRq.builder()
                .status(Status.FINISHED)
                .build();


        mockMvc.perform(put("/api/v1/task/status/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenUser)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("FINISHED"));
    }

    @Test
    @DisplayName("Edit status of task other, then return error message")
    public void whenEditStatusTaskOther_thenReturnErrorMessage() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");

        User user = createUser("user", "user@mail.com", RoleType.ROLE_USER, "user");
        refreshTokenService.createRefreshToken(user.getId());
        setAuthenticationInContext(user, "user");
        String tokenUser = getTokenFromAuthRs(user.getEmail(), "user");

        Task task = createTask("Task1", "description task1", Status.WAITING, Priority.MEDIUM, admin, admin, null);
        Comment comment1 = createComment("Comment1", admin, task);
        Comment comment2 = createComment("Comment2", user, task);
        task.addComment(comment1);
        task.addComment(comment2);

        UpsertTaskRq rq = UpsertTaskRq.builder()
                .status(Status.FINISHED)
                .build();
        var fakeToken = tokenAdmin + "1";

        mockMvc.perform(put("/api/v1/task/status/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, fakeToken)
                        .content(objectMapper.writeValueAsString(rq))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Full authentication is required to access this resource"));
    }


    @Test
    @DisplayName("Delete task, then delete task from repository")
    public void whenDeleteTask_thenDeleteFromRepo() throws Exception {
        User admin = createUser("admin", "admin@mail.com", RoleType.ROLE_ADMIN, "admin");
        refreshTokenService.createRefreshToken(admin.getId());
        setAuthenticationInContext(admin, "admin");
        String tokenAdmin = getTokenFromAuthRs(admin.getEmail(), "admin");


        Task task = createTask("Task1", "description task1", Status.WAITING, Priority.MEDIUM, admin, admin, null);
        Comment comment1 = createComment("Comment1", admin, task);
        task.addComment(comment1);

        Long countBefore = taskRepository.count();

        mockMvc.perform(delete("/api/v1/task/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, tokenAdmin)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        var countAfter = taskRepository.count();
        assertEquals(countBefore, (countAfter + 1));
    }




}