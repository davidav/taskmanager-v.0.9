package com.example.taskmanager.aop;


import com.example.taskmanager.entity.RoleType;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.stream.Collectors;
@RequiredArgsConstructor
public class WithMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMocUser> {

    private final AuthenticationManager authenticationManager;

    @Override
    public SecurityContext createSecurityContext(WithMocUser annotation) {
        var authorities = Arrays.stream(annotation.authorities())
                .map(RoleType::valueOf)
                .collect(Collectors.toSet());

        User user = User.builder()
                .id(annotation.userId())
                .username(annotation.username())
                .email(annotation.email())
                .password(annotation.password())
                .roles(authorities)
                .build();

        AppUserDetails userDetails = AppUserDetails.builder()
                .user(user)
                .build();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetails.getEmail(),
                        userDetails.getPassword()
                )));
        return context;
    }
}
