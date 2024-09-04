package com.example.taskmanager.dto.task;


import com.example.taskmanager.validation.TaskFilterValid;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@TaskFilterValid
@Schema(description = "Task Filter")
public class TaskFilter {

    private Integer pageSize;
    private Integer pageNumber;
    private Long authorId;
    private Long executorId;

}
