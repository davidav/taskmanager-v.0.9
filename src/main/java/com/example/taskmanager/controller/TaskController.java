package com.example.taskmanager.controller;

import com.example.taskmanager.dto.ErrorRs;
import com.example.taskmanager.dto.task.*;
import com.example.taskmanager.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/task")
@Tag(name = "Task", description = "Task API")
public class TaskController {

    private final TaskService taskService;


    @Operation(
            summary = "Get Task by id",
            description = "Return task and list of comments related with it.  " +
                    "Allowed to all registered users"
//            tags = {"task", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public TaskRs findById(@PathVariable Long id) {
        log.info("TaskController -> findById {}", id);
        return taskService.findByIdRs(id);
    }

    @Operation(
            summary = "Create new task",
            description = "Return created task." +
                    "Allowed to all registered users"
//            tags = {"task", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @PostMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public TaskRs create(@RequestBody @Valid UpsertTaskRq rq,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> create task: {}", rq.getTitle());
        return taskService.create(rq, userDetails);
    }

    @Operation(
            summary = "Edit task",
            description = "Return edited task." +
                    "Allowed to task's author."
//            tags = {"task"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public TaskRs update(@PathVariable Long id, @RequestBody UpsertTaskRq rq,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> update id={} rq={}", id, rq);
        return taskService.update(id, rq, userDetails);
    }

    @Operation(
            summary = "Edit status of task",
            description = "Author or performer available change status of task"
//            tags = {"task"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @PutMapping("/status/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public TaskRs updateStatus(@PathVariable Long id, @RequestBody UpsertStatusRq rq,
                         @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> updateStatus id={} rq={}", id, rq);
        return taskService.updateStatus(id, rq, userDetails);
    }


    @Operation(
            summary = "Delete task",
            description = "Delete task with a specific ID. " +
                    "Available to task's author"
//            tags = {"task", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @DeleteMapping("/{id}")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public void deleteById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("TaskController -> deleteById {}", id);
        taskService.deleteById(id, userDetails);
    }

    @Operation(
            summary = "Filtering tasks",
            description = "Returns tasks of a specific author or performer, " +
                    "as well as all comments on them, providing filtering " +
                    "and pagination of the output. " +
                    "Allowed to all registered users"
//            tags = {"task", "id"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = TaskListRs.class), mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorRs.class), mediaType = "application/json")}
            )
    })
    @GetMapping("/filter")
    @PreAuthorize(value = "hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public TaskListRs findAllByFilter(@Valid TaskFilter filter) {
        log.info("TaskController -> findAllByFilter {}", filter);
        return taskService.filterBy(filter);
    }

}
