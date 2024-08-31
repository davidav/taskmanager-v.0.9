package com.example.taskmanager.dto.task;


import com.example.taskmanager.validation.TaskFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TaskFilterValid
public class TaskFilter {

    private Integer pageSize;
    private Integer pageNumber;
    private Long authorId;
    private Long executorId;

}
