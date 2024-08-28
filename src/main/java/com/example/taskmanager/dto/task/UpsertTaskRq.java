package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpsertTaskRq {

    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private Long executorId;

}
