package com.example.taskmanager.service;

import com.example.taskmanager.dto.task.*;
import com.example.taskmanager.entity.Task;
import org.springframework.security.core.userdetails.UserDetails;

public interface TaskService {

    Task findById(Long id);

    TaskRs findByIdRs(Long id);

    TaskRs create(UpsertTaskRq request, UserDetails userDetailsd);

    TaskRs update(Long id, UpsertTaskRq request, UserDetails userDetails);

    TaskRs updateStatus(Long id, UpsertStatusRq rq, UserDetails userDetails);

    void deleteById(Long id, UserDetails userDetails);

    TaskListRs filterBy(TaskFilter filter);
}
