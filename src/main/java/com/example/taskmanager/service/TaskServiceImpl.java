package com.example.taskmanager.service;

import com.example.taskmanager.dto.mapper.TaskMapper;
import com.example.taskmanager.dto.task.TaskFilter;
import com.example.taskmanager.dto.task.TaskListRs;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.dto.task.TaskRs;
import com.example.taskmanager.entity.Task;
import com.example.taskmanager.repo.TaskRepository;
import com.example.taskmanager.repo.TaskSpecification;
import com.example.taskmanager.security.SecurityService;
import com.example.taskmanager.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final SecurityService securityService;

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
        task.setAuthor(securityService.getByUsername(userDetails.getUsername()));
        return taskMapper.taskToResponse(taskRepository.save(task));

    }

    @Override
//TODO @TaskEditAvailable
    public TaskRs update(Long id, UpsertTaskRq rq, UserDetails userDetails) {

        Task existedTask = findById(id);
        AppHelperUtils.copyNonNullProperties(rq, existedTask);

        return taskMapper.taskToResponse(taskRepository.save(existedTask));

    }

    @Override
//    TODO @TaskDeleteAvailable
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
