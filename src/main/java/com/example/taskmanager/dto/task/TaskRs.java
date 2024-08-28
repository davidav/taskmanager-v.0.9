package com.example.taskmanager.dto.task;


import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.entity.Priority;
import com.example.taskmanager.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class TaskRs {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private Long authorId;
    private Long executorId;
    private Instant createdAt;
    private List<CommentRs> comments;

}
