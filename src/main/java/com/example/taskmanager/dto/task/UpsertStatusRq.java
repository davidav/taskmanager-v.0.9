package com.example.taskmanager.dto.task;

import com.example.taskmanager.entity.Status;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpsertStatusRq {

    private Status status;

}
