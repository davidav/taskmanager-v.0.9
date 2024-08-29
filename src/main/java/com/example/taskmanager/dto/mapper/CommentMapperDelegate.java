package com.example.taskmanager.dto.mapper;


import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.dto.comment.UpsertCommentRq;
import com.example.taskmanager.entity.Comment;
import com.example.taskmanager.security.SecurityService;
import com.example.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;



public abstract class CommentMapperDelegate implements CommentMapper{

    @Autowired
    private SecurityService securityService;
    @Autowired
    private TaskService taskService;


    @Override
    public Comment requestToComment(UpsertCommentRq rq, UserDetails userDetails) {

        return Comment.builder()
                .comment(rq.getComment())
                .author(securityService.getByUsername(userDetails.getUsername()))
                .task(taskService.findById(rq.getTaskId()))
                .build();
    }

    @Override
    public CommentRs commentToResponse(Comment comment) {

        return CommentRs.builder()
                .id(comment.getId())
                .comment(comment.getComment())
                .authorId(comment.getAuthor().getId())
                .taskId(comment.getTask().getId())
                .createdAt(comment.getCreateAt())
                .build();
    }
}
