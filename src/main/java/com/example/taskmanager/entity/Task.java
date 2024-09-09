package com.example.taskmanager.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private Status status = Status.WAITING;

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @ManyToOne()
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    @JsonIgnore
    private User author;

    @ManyToOne()
    @JoinColumn(name = "executor_id")
    @ToString.Exclude
    @JsonIgnore
    private User executor;

    @CreationTimestamp
    private Instant createdAt;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    public void addComment(Comment comment) {
        if (comments == null){
            comments = new ArrayList<>();
        }
        comments.add(comment);
    }

}
