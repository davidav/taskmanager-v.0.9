package com.example.taskmanager.dto.comment;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpsertCommentRq {

    @NotNull(message = "taskId must be filled")
    @Positive(message = "taskId must be more 0")
    private Long taskId;

    @NotBlank(message = "comment must be filled")
    @Size(min = 3, max = 300, message = "comment must be from {min} to {max} symbols")
    private String comment;

}
