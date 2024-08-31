package com.example.taskmanager.security;

import com.example.taskmanager.exception.AlreadyExistsException;
import com.example.taskmanager.dto.ErrorRs;
import com.example.taskmanager.dto.auth.*;
import com.example.taskmanager.repo.UserRepository;
import com.example.taskmanager.security.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final SecurityService securityService;

    @PostMapping("/signin")
    public ResponseEntity<AuthRs> authUser(@RequestBody LoginRq loginRq) {
        return ResponseEntity.ok(securityService.authenticateUser(loginRq));
    }

    @PostMapping("/register")
    public ResponseEntity<ErrorRs> registerUser(@RequestBody UpsertUserRq createUserRequest) {
        if (userRepository.existsByUsername(createUserRequest.getUsername())) {
            throw new AlreadyExistsException("Username already exist");
        }

        if (userRepository.existsByEmail(createUserRequest.getEmail())) {
            throw new AlreadyExistsException("Email already exist");
        }

        securityService.register(createUserRequest);

        return ResponseEntity.ok(new ErrorRs("User created"));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenRs> refreshToken(@RequestBody RefreshTokenRq request) {
        return ResponseEntity.ok(securityService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<ErrorRs> logoutUser(@AuthenticationPrincipal UserDetails userDetails){
        securityService.logout();

        return ResponseEntity.ok(new ErrorRs("User logout. Username is: " + userDetails.getUsername()));
    }


}
