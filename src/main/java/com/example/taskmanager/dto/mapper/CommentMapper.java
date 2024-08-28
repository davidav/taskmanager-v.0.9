package com.example.taskmanager.dto.mapper;


import com.example.taskmanager.dto.comment.CommentListRs;
import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.dto.comment.UpsertCommentRq;
import com.example.taskmanager.entity.Comment;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.stream.Collectors;

@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {

    default CommentListRs commentListToCommentListResponse(List<Comment> comments){
        CommentListRs rs = new CommentListRs();
        rs.setComments(comments.stream().map(this::commentToResponse).collect(Collectors.toList()));
        return rs;
    }

    CommentRs commentToResponse(Comment comment);

    Comment requestToComment(UpsertCommentRq rq, UserDetails userDetails);

}
