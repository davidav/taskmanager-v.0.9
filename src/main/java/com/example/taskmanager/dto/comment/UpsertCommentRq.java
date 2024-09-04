package com.example.taskmanager.dto.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Comment request")
public class UpsertCommentRq {

    @Schema(description = "Task id", example = "1")
    @NotNull(message = "taskId must be filled")
    @Positive(message = "taskId must be more 0")
    private Long taskId;

    @Schema(description = "Comment", example = "Explain the task in detail")
    @NotBlank(message = "Comment must be filled")
    @Size(min = 3, max = 300, message = "Comment must be from {min} to {max} symbols")
    private String comment;

}
