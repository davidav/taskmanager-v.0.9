package com.example.taskmanager.service;


import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.dto.comment.UpsertCommentRq;
import com.example.taskmanager.dto.mapper.CommentMapper;
import com.example.taskmanager.entity.Comment;
import com.example.taskmanager.repo.CommentRepository;
import com.example.taskmanager.util.AppHelperUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.helpers.MessageFormatter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    public final CommentRepository commentRepository;
    public final CommentMapper commentMapper;


    public Comment findById(Long id) {

        return commentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                MessageFormatter.format("Comment with id {} not found", id).getMessage()));
    }

    @Override
    public CommentRs findByIdRs(Long id) {

        return commentMapper.commentToResponse(findById(id));
    }

    @Override
    public CommentRs create(UpsertCommentRq rq, UserDetails userDetails) {

        Comment comment = commentMapper.requestToComment(rq, userDetails);

        return commentMapper.commentToResponse(commentRepository.save(comment));
    }

    @Override
    //    TODO @CommentEditAvailable
    public CommentRs update(Long id, UpsertCommentRq rq, UserDetails userDetails) {

        Comment existComment = findById(id);
        AppHelperUtils.copyNonNullProperties(commentMapper.requestToComment(rq, userDetails), existComment);

        return commentMapper.commentToResponse(commentRepository.save(existComment));

    }

    @Override
    //    TODO @CommentDeleteAvailable
    public void deleteById(Long id, UserDetails userDetails) {
        commentRepository.deleteById(id);
    }
}
