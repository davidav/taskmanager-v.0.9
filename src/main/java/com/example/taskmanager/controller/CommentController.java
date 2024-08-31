package com.example.taskmanager.controller;


import com.example.taskmanager.dto.ErrorRs;
import com.example.taskmanager.dto.comment.CommentRs;
import com.example.taskmanager.dto.comment.UpsertCommentRq;
import com.example.taskmanager.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
@Tag(name = "Comment", description = "Comment API")
public class CommentController {

    private final CommentService commentService;

    @Operation(
            summary = "Get comment by id",
            description = "Return comment with a specific ID. " +
                    "Allowed to all registered users",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public CommentRs findById(@PathVariable Long id) {
        return commentService.findByIdRs(id);
    }

    @Operation(
            summary = "Create new comment",
            description = "Return created comment. " +
                    "Allowed to all registered users",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = CommentRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public CommentRs create(@RequestBody @Valid UpsertCommentRq rq,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        return commentService.create(rq, userDetails);

    }

    @Operation(
            summary = "Edit comment",
            description = "Return edited comment. Only the creator comment can edit it",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = CommentRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public CommentRs update(@PathVariable Long id, @RequestBody @Valid UpsertCommentRq request,
                                            @AuthenticationPrincipal UserDetails userDetails) {

        return commentService.update(id, request, userDetails);

    }

    @Operation(
            summary = "Delete comment",
            description = "Delete comment with a specific ID. " +
                    "Available only to users with a roles ADMIN",
            tags = {"comment", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204"
            ),
            @ApiResponse(
                    responseCode = "401",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasRole('ROLE_ADMIN')")
    public void deleteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        commentService.deleteById(id, userDetails);
    }
}
