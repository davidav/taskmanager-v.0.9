package com.example.taskmanager.service;

import com.example.taskmanager.aop.TaskDeleteAvailable;
import com.example.taskmanager.aop.TaskEditAvailable;
import com.example.taskmanager.aop.TaskEditStatusAvailable;
import com.example.taskmanager.dto.mapper.TaskMapper;
import com.example.taskmanager.dto.task.*;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.repo.TaskSpecification;
import com.example.taskmanager.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        MessageFormatter.format("Task with id {} not found", id).getMessage()));
    }

    @Override
    public TaskRs findByIdRs(Long id) {
        return taskMapper.taskToResponse(findById(id));
    }

    @Override
    public TaskRs create(UpsertTaskRq rq, UserDetails userDetails) {
        Task task = taskMapper.requestToTask(rq, userDetails);

        return taskMapper.taskToResponse(taskRepository.save(task));

    }

    @Override
    @TaskEditAvailable
    public TaskRs update(Long id, UpsertTaskRq rq, UserDetails userDetails) {
        log.info("TaskService -> update {}", id);
        Task existedTask = findById(id);
        AppHelperUtils.copyNonNullProperties(taskMapper.requestToTask(rq, userDetails), existedTask);

        return taskMapper.taskToResponse(taskRepository.save(existedTask));

    }

    @Override
    @TaskEditStatusAvailable
    public TaskRs updateStatus(Long id, UpsertStatusRq rq, UserDetails userDetails) {
        log.info("TaskService -> updateStatus ");
        Task existedTask = findById(id);
        existedTask.setStatus(rq.getStatus());

        return taskMapper.taskToResponse(taskRepository.save(existedTask));

    }



    @Override
    @TaskDeleteAvailable
    public void deleteById(Long id, UserDetails userDetails) {
        taskRepository.deleteById(id);
    }

    @Override
    public TaskListRs filterBy(TaskFilter filter) {
        Specification<Task> spec = TaskSpecification.withFilter(filter);
        PageRequest pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize());
        List<Task> tasks = taskRepository.findAll(spec, pageable).getContent();

        return taskMapper.taskListToTaskListResponse(tasks);
    }
}
