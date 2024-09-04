package com.example.taskmanager.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Refresh token response")
public class RefreshTokenRs {

    @Schema(description = "Access token")
    private String accessToken;

    @Schema(description = "Refresh token")
    private String refreshToken;
}