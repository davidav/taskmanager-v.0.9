package com.example.taskmanager.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Authentication request")
public class LoginRq {

    @Schema(description = "Email address", example = "jondoe@gmail.com")
    @Size(min = 5, max = 255, message = "Email address must be between 5 and 255 characters")
    @NotBlank(message = "Email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;

    @Schema(description = "password", example = "my_1secret1_password")
    @Size(min = 4, max = 255, message = "The password length must be from 4 no more than 255 characters.")
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
