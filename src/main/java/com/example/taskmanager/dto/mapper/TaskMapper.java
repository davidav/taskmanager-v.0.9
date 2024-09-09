package com.example.taskmanager.dto.mapper;

import com.example.taskmanager.dto.task.UpsertTaskRq;
import com.example.taskmanager.dto.task.TaskListRs;
import com.example.taskmanager.dto.task.TaskRs;
import com.example.taskmanager.entity.Task;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(TaskMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TaskMapper {

    default TaskListRs taskListToTaskListResponse(List<Task> tasks){
        TaskListRs rs = new TaskListRs();
        rs.setTasks(tasks.stream().map(this::taskToResponse).collect(Collectors.toList()));
        return rs;
    }

    Task requestToTask(UpsertTaskRq rq, UserDetails userDetails);

    TaskRs taskToResponse(Task task);
}
