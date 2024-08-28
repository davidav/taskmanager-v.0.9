package com.example.taskmanager.dto;


import com.example.taskmanager.validation.PagesFilterValid;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@PagesFilterValid
public class PagesRq {

    private Integer pageSize;

    private Integer pageNumber;
}
