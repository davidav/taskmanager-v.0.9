package com.example.taskmanager.dto.mapper;

import com.example.taskmanager.dto.task.TaskRs;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.stream.Collectors;


public abstract class TaskMapperDelegate implements TaskMapper {


    @Autowired
    private SecurityService securityService;
    @Autowired
    private CommentMapper commentMapper;


    public TaskRs taskToResponse(Task task) {

        TaskRs taskRs = TaskRs.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .authorId(task.getAuthor().getId())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .comments(task.getComments().stream()
                        .map(commentMapper::commentToResponse)
                        .collect(Collectors.toList()))
                .build();

        if (task.getExecutor() != null){
            taskRs.setExecutorId(task.getExecutor().getId());
        }

        return taskRs;
    }

    @Override
    public Task requestToTask(UpsertTaskRq rq, UserDetails userDetails) {
        Task task = Task.builder()
                .title(rq.getTitle())
                .description(rq.getDescription())
                .author(securityService.getByEmail(userDetails.getUsername()))
                .status(rq.getStatus())
                .priority(rq.getPriority())
                .build();

        if (rq.getExecutorId() != null) {
            task.setExecutor(securityService.getById(rq.getExecutorId()));
        }

        return task;
    }

}
