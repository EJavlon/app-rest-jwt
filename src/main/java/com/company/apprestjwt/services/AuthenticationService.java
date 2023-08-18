package com.company.apprestjwt.services;

import com.company.apprestjwt.dto.AuthenticationRequest;
import com.company.apprestjwt.dto.AutheticatinResponse;
import com.company.apprestjwt.dto.RegisterRequestDto;
import com.company.apprestjwt.entity.User;
import com.company.apprestjwt.enums.Role;
import com.company.apprestjwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AutheticatinResponse register(RegisterRequestDto requestDto) {
        var user = User.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var jwtToken = jwtService.genereteToken(user);

        return AutheticatinResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AutheticatinResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
             new UsernamePasswordAuthenticationToken(
                     request.getUsername(),
                     request.getPassword()
             )
        );

        var user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwtToken = jwtService.genereteToken(user);

        return AutheticatinResponse.builder()
                .token(jwtToken)
                .build();
    }
}
