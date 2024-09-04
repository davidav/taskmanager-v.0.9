package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.Status;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Task's status request")
public class UpsertStatusRq {

    @Schema(description = "Task status -  WAITING, RUNNING, FINISHED", example = "WAITING")
    @NotBlank(message = "Status must be filled")
    private Status status;

}
