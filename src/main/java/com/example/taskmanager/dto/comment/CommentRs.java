package com.example.taskmanager.dto.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;


import java.time.Instant;

@Data
@Builder
@Schema(description = "Comments response")
public class CommentRs {

    private Long id;

    private String comment;

    private Long authorId;

    private Long taskId;

    private Instant createdAt;

}
