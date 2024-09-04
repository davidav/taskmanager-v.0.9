package com.example.taskmanager.dto.auth;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Response with access token")
public class AuthRs {

    @Schema(description = "User id")
    private Long id;

    @Schema(description = "Access token")
    private String token;

    @Schema(description = "Refresh token")
    private String refreshToken;

    @Schema(description = "username")
    private String username;

    @Schema(description = "email")
    private String email;

    @Schema(description = "List of roles")
    private List<String> roles;

}
