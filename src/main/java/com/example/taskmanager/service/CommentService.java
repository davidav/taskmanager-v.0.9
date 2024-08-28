package com.example.taskmanager.service;


import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.dto.comment.UpsertCommentRq;

import org.springframework.security.core.userdetails.UserDetails;



public interface CommentService {

    CommentRs findByIdRs(Long id);

    CommentRs create(UpsertCommentRq rq, UserDetails userDetails);

    CommentRs update(Long id, UpsertCommentRq rq, UserDetails userDetails);

    void deleteById(Long id, UserDetails userDetails);

}
