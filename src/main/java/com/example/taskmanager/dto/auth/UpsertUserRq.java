package com.example.taskmanager.dto.auth;

import com.example.taskmanager.entity.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Registration request")
public class UpsertUserRq {

    @Schema(description = "Username", example = "Jon")
    @Size(min = 5, max = 50, message = "Username must be between 5 and 50 characters")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Schema(description = "Email address", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;

    @Schema(description = "Roles", example =("[\"ROLE_USER\"]"))
    private Set<RoleType> roles;

    @Schema(description = "Password", example = "7KjHdsBse_")
    @Size(max = 255, message = "The password length must be no more than 255 characters.")
    private String password;

}
