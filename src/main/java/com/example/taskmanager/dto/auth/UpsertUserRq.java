package com.example.taskmanager.dto.auth;

import com.example.taskmanager.entity.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpsertUserRq {

    private String username;
    private String email;
    private Set<RoleType> roles;
    private String password;

}
