package com.example.taskmanager.dto.comment;


import lombok.Builder;
import lombok.Data;


import java.time.Instant;

@Data
@Builder
//@UserFilterValid
public class CommentRs {

    private Long id;

    private String comment;

    private Long authorId;

    private Long taskId;

    private Instant createdAt;

}
