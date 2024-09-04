package com.example.taskmanager.dto.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@Schema(description = "List of tasks response")
public class TaskListRs {

    private List<TaskRs> tasks;

}
