package com.example.taskmanager.dto.comment;



import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor

public class CommentListRs {

    private List<CommentRs> comments;

}
