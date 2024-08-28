package com.example.taskmanager.dto.task;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
public class TaskListRs {

    private List<TaskRs> tasks;

}
