package com.example.taskmanager.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Priority priority;

    @OneToOne
    private User author;

    @OneToOne
    private User executor;

}
