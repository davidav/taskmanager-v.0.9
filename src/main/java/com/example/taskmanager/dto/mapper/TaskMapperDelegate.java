package com.example.taskmanager.dto.mapper;

import com.example.taskmanager.dto.task.TaskRs;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public abstract class TaskMapperDelegate implements TaskMapper {

    @Autowired
    private final CommentMapper commentMapper;
    @Autowired
    private final SecurityService securityService;

    public TaskRs taskToResponse(Task task){

        return TaskRs.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .authorId(task.getAuthor().getId())
                .executorId(task.getExecutor().getId())
                .status(task.getStatus())
                .priority(task.getPriority())
                .createdAt(task.getCreatedAt())
                .comments(task.getComments().stream()
                        .map(commentMapper::commentToResponse)
                        .collect(Collectors.toList()))
                .build();

    }

    @Override
    public Task requestToTask(UpsertTaskRq rq, UserDetails userDetails) {
        return Task.builder()
                .title(rq.getTitle())
                .description(rq.getDescription())
                .author(securityService.getByUsername(userDetails.getUsername()))
                .executor(securityService.getById(rq.getExecutorId()))
                .status(rq.getStatus())
                .priority(rq.getPriority())
                .build();

    }
    @Override
    public Task requestToTask(Long id, UpsertTaskRq rq, UserDetails userDetails){
        Task task = requestToTask(rq, userDetails);
        task.setId(id);

        return task;

    }
}
