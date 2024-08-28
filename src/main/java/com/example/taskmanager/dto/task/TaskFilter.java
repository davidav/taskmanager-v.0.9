package com.example.taskmanager.dto.task;


import com.example.taskmanager.validation.PagesFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PagesFilterValid
public class TaskFilter {

    private Integer pageSize;
    private Integer pageNumber;
    private Long authorId;
    private Long executorId;

}
