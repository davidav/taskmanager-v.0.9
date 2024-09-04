package com.example.taskmanager.dto.comment;



import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Schema(description = "List of comments response")
public class CommentListRs {

    @Schema(description = "List of comments")
    private List<CommentRs> comments;

}
