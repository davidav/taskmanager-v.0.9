package com.example.taskmanager.aop;

import com.example.taskmanager.dto.task.UpsertStatusRq;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.security.SecurityService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.websocket.AuthenticationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TaskAspect {

    private final TaskRepository taskRepository;
    private final SecurityService securityService;

    @Before("@annotation(com.example.taskmanager.aop.TaskEditAvailable)")
    public void editBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        UpsertTaskRq taskRq = (UpsertTaskRq) args[1];
        UserDetails userDetails = (UserDetails) args[2];

        Task existTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormatter.format("Task with id {} not found", id).getMessage()));
        User requestUser = securityService.getByUsername(userDetails.getUsername());

        if (!Objects.equals(requestUser.getId(), existTask.getAuthor().getId())) {
            throw new AuthenticationException("Edit task available author.");
        }

    }

    @Before("@annotation(com.example.taskmanager.aop.TaskEditStatusAvailable)")
    public void editStatusBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        UserDetails userDetails = (UserDetails) args[2];

        Task existTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormatter.format("Task with id {} not found", id).getMessage()));
        User requestUser = securityService.getByUsername(userDetails.getUsername());

        if (!Objects.equals(requestUser.getId(), existTask.getExecutor().getId())){
            throw new AuthenticationException("Changing task's status available performer");
        }

    }

    @Before("@annotation(com.example.taskmanager.aop.TaskDeleteAvailable)")
    public void deleteBefore(JoinPoint joinPoint) throws AuthenticationException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        UserDetails userDetails = (UserDetails) args[1];
        Task existTask = taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormatter.format("Task with id {} not found", id).getMessage()));
        User requestUser = securityService.getByUsername(userDetails.getUsername());
        if (!Objects.equals(requestUser.getId(), existTask.getAuthor().getId())) {
            throw new AuthenticationException("Delete task available author");
        }
    }
}
