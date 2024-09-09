package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Task request")
public class UpsertTaskRq {

    @Schema(description = "Task title", example = "Take a photo")
    @NotBlank(message = "Title must be filled")
    @Size(min = 3, max = 50, message = "Title must be from {min} to {max} symbols")
    private String title;

    @Schema(description = "Task description. Possible values - ", example = "Take a photo of something beautiful")
    @NotBlank(message = "Description must be filled")
    @Size(min = 3, max = 300, message = "Description must be from {min} to {max} symbols")
    private String description;

    @Schema(description = "Task priority - HIGH, MEDIUM, LOW", example = "LOW")
    private Priority priority;

    @Schema(description = "Task status - WAITING, RUNNING, FINISHED", example = "WAITING")
    private Status status;

    @Schema(description = "Performer id", example = "1L")
    private Long executorId;

}
