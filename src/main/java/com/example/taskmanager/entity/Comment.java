package com.example.taskmanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String comment;

    @OneToOne
    private User author;

    private Instant create;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id")
    @ToString.Exclude
    private Task task;

}
