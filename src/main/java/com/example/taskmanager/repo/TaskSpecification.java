package com.example.taskmanager.repo;

import com.example.taskmanager.dto.task.TaskFilter;
import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.domain.Specification;


public interface TaskSpecification {

    static Specification<Task> withFilter(TaskFilter filter) {
        return Specification.where(byAuthorId(filter.getAuthorId()))
                        .and(byExecutorId(filter.getExecutorId()));
    }

    static Specification<Task> byExecutorId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }


    static Specification<Task> byAuthorId(Long id) {
            return (root, query, criteriaBuilder) -> {
                if (id == null){
                    return null;
                }
                return criteriaBuilder.equal(root.get("id"), id);
            };

    }
}
