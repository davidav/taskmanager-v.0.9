package com.example.taskmanager.repo;

import com.example.taskmanager.dto.task.TaskFilter;
import com.example.taskmanager.entity.Task;
import org.springframework.data.jpa.domain.Specification;


public interface TaskSpecification {

    static Specification<Task> withFilter(TaskFilter filter) {
        return Specification.where(byAuthorId(filter.getAuthorId()))
                        .or(byExecutorId(filter.getExecutorId()));
    }

    static Specification<Task> byExecutorId(Long executorId) {
        return (root, query, criteriaBuilder) -> {
            if (executorId == null){
                return null;
            }
            return criteriaBuilder.equal(root.get("executor").get("id"), executorId);
        };
    }


    static Specification<Task> byAuthorId(Long authorId) {
            return (root, query, criteriaBuilder) -> {
                if (authorId == null){
                    return null;
                }
                return criteriaBuilder.equal(root.get("author").get("id"), authorId);
            };

    }
}
