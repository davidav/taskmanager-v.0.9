package com.example.taskmanager.service;

import com.example.taskmanager.dto.mapper.TaskMapper;
import com.example.taskmanager.dto.task.TaskRs;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.entity.*;
import com.example.taskmanager.repo.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    @Mock
    TaskRepository taskRepository;
    @Mock
    TaskMapper taskMapper;
    @Mock
    SecurityService securityService;
    @Mock
    UserDetails userDetails;
    @InjectMocks
    TaskServiceImpl taskService;
    @BeforeEach
    void setUp() {

    }

    @Test
    void whenFindByIdSuccess() {
        User user = User.builder()
                .id(1L)
                .username("user")
                .email("user@mail.com")
                .password("$2a$10$uN9oAjVnj/m2EPrvRIL6/ef68XYThJjeniAE8jurdWaGvMbfCemjG")
                .roles(Set.of(RoleType.ROLE_USER))
                .build();
        Task task = Task.builder()
                .id(1L)
                .title("Task_1")
                .description("Description task 1")
                .priority(Priority.HIGH)
                .status(Status.WAITING)
                .author(user)
                .build();

        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.of(task));
        Task returnedTask = taskService.findById(1L);

        assertThat(returnedTask.getId().equals(task.getId()));
        assertThat(returnedTask.getTitle().equals(task.getTitle()));
        assertThat(returnedTask.getDescription().equals(task.getDescription()));
        assertThat(returnedTask.getAuthor().equals(task.getAuthor()));
        assertThat(returnedTask.getStatus().equals(task.getStatus()));
        assertThat(returnedTask.getPriority().equals(task.getPriority()));
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void whenFindByIdNotFound(){

        given(taskRepository.findById(Mockito.anyLong())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> {
            Task returnedTask = taskService.findById(1L);
        });

        assertThat(thrown)
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Task with id 1 not found");
        verify(taskRepository, times(1)).findById(1L);
    }

@Test
    void whenCreateTaskSuccess() {
    User user = User.builder()
            .id(1L)
            .username("user")
            .email("user@mail.com")
            .password("$2a$10$uN9oAjVnj/m2EPrvRIL6/ef68XYThJjeniAE8jurdWaGvMbfCemjG")
            .roles(Set.of(RoleType.ROLE_USER))
            .build();
    Task task = Task.builder()
            .id(1L)
            .title("Task_1")
            .description("Description task 1")
            .priority(Priority.HIGH)
            .status(Status.WAITING)
            .author(user)
            .build();
    UpsertTaskRq rq = UpsertTaskRq.builder()
            .title("Task_1")
            .description("Description task 1")
            .priority(Priority.HIGH)
            .status(Status.WAITING)
            .build();
    TaskRs taskRs = TaskRs.builder()
            .title("Task_1")
            .description("Description task 1")
            .status(Status.WAITING)
            .priority(Priority.HIGH)
            .build();

    given(taskRepository.save(task)).willReturn(task);
    given(securityService.getByEmail(Mockito.anyString())).willReturn(user);
    given(taskMapper.taskToResponse(task)).willReturn(taskRs);
    given(taskMapper.requestToTask(rq, userDetails)).willReturn(task);

    taskRs = taskService.create(rq, userDetails);

    assertThat(taskRs.getTitle().equals(rq.getTitle()));

}


}