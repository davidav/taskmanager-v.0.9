package com.example.taskmanager.service;

import com.example.taskmanager.dto.auth.*;
import com.example.taskmanager.entity.RefreshToken;
import com.example.taskmanager.entity.User;
import com.example.taskmanager.exception.RefreshTokenException;
import com.example.taskmanager.repo.UserRepository;
import com.example.taskmanager.security.AppUserDetails;
import com.example.taskmanager.security.jwt.JwtUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthRs authenticateUser(LoginRq loginRq){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRq.getEmail(),
                loginRq.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return AuthRs.builder()
                .id(userDetails.getId())
                .token(jwtUtils.generateJwtToken(userDetails))
                .refreshToken(refreshToken.getToken())
                .username(userDetails.getRealUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    public void register(UpsertUserRq upsertUserRq){
        var user = User.builder()
                .username(upsertUserRq.getUsername())
                .email(upsertUserRq.getEmail())
                .password(passwordEncoder.encode(upsertUserRq.getPassword()))
                .build();
        user.setRoles(upsertUserRq.getRoles());

        userRepository.save(user);
    }

    public JwtAndRefreshTokenRs refreshToken(RefreshTokenRq refreshTokenRq){
        String requestRefreshToken = refreshTokenRq.getRefreshToken();
        return refreshTokenService.findByRefreshToken(requestRefreshToken)
                .map(refreshTokenService::checkRefreshToken)
                .map(RefreshToken::getUserId)
                .map(userId ->{
                    User tokenOwner = userRepository.findById(userId).orElseThrow(() ->
                            new   RefreshTokenException("Exception trying to get token for Id: {}" + userId));

                    String token = jwtUtils.generateJwtTokenFromEmail(tokenOwner.getEmail());

                    return new JwtAndRefreshTokenRs(token, refreshTokenService.createRefreshToken(userId).getToken());

                }).orElseThrow(() -> new RefreshTokenException(requestRefreshToken, "Refresh token not found"));

    }

    public void logout(){
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails){
            Long userId = userDetails.getId();

            refreshTokenService.deleteRefreshTokenByUserId(userId);
        }
    }

    public User getByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    public User getById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
    }



}
