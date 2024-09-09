package com.example.taskmanager.controller;

import com.example.taskmanager.dto.InfoRs;
import com.example.taskmanager.exception.AlreadyExistsException;
import com.example.taskmanager.dto.auth.*;
import com.example.taskmanager.repo.UserRepository;
import com.example.taskmanager.service.SecurityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "User registration and login")
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/signin")
    public AuthRs authUser(@RequestBody @Valid LoginRq loginRq) {
        return securityService.authenticateUser(loginRq);
    }

    @PostMapping("/register")
    public InfoRs registerUser(@RequestBody @Valid UpsertUserRq upsertUserRq) {

        if (userRepository.existsByUsername(upsertUserRq.getUsername())) {
            throw new AlreadyExistsException("Username already exist");
        }

        if (userRepository.existsByEmail(upsertUserRq.getEmail())) {
            throw new AlreadyExistsException("Email already exist");
        }

        securityService.register(upsertUserRq);

        return new InfoRs("User created");
    }

    @PostMapping("/refresh-token")
    public JwtAndRefreshTokenRs refreshToken(@RequestBody RefreshTokenRq rq) {
        return securityService.refreshToken(rq);
    }

    @PostMapping("/logout")
    public InfoRs logoutUser(@AuthenticationPrincipal UserDetails userDetails){
        securityService.logout();

        return new InfoRs("User logout. Username is: " + userDetails.getUsername());
    }


}
