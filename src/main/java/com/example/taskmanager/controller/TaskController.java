package com.example.taskmanager.controller;

import com.example.taskmanager.dto.task.TaskFilter;
import com.example.taskmanager.dto.task.TaskListRs;
import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.dto.task.TaskRs;

import com.example.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public TaskRs findById(@PathVariable Long id) {
        log.info("TaskController -> findById {}", id);
        return taskService.findByIdRs(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TaskRs create(@RequestBody @Valid UpsertTaskRq rq,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> create task: {}", rq.getTitle());
        return taskService.create(rq, userDetails);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public TaskRs update(@PathVariable Long id, @RequestBody UpsertTaskRq rq,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> update id={} rq={}", id, rq);
        return taskService.update(id, rq, userDetails);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> deleteById {}", id);
        taskService.deleteById(id, userDetails);
    }

    @GetMapping("/filter")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public TaskListRs findAllByFilter(@Valid TaskFilter filter) {
        log.info("TaskController -> findAllByFilter {}", filter);
        return taskService.filterBy(filter);
    }

}
