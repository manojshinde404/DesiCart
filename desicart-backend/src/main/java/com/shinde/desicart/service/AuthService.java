package com.shinde.desicart.service;

import com.shinde.desicart.dto.JwtResponse;
import com.shinde.desicart.dto.LoginRequest;
import com.shinde.desicart.dto.MessageResponse;
import com.shinde.desicart.dto.SignupRequest;
import com.shinde.desicart.exception.CustomException;
import com.shinde.desicart.model.User;
import com.shinde.desicart.security.JwtUtils;
import com.shinde.desicart.security.UserDetailsImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserService userService,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getAuthorities().stream()
                        .map(item -> item.getAuthority())
                        .collect(Collectors.toList()));
    }

    public MessageResponse registerUser(SignupRequest signupRequest) throws CustomException {
        // Validation
        if (userService.existsByUsername(signupRequest.getUsername())) {
            throw new CustomException("Username is already taken!");
        }
        if (userService.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException("Email is already in use!");
        }

        // Create user
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                signupRequest.getName(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        // Save user
        userService.saveUser(user);

        // Return success message
        return new MessageResponse("User registered successfully!");
    }
}
