package com.example.taskmanager.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Refresh token request")
public class RefreshTokenRq {

    @Schema(description = "Refresh token")
    private String refreshToken;

}