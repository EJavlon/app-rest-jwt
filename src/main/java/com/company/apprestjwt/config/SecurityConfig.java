package com.company.apprestjwt.config;

import com.company.apprestjwt.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class  SecurityConfig{

    private final JwtAuthenticationFilter jwtFilter;

    private final AuthenticationProvider authenticationProvider;

    private final PasswordEncoder passwordEncoder;


    @Bean
    @SneakyThrows
    public UserDetailsManager userDetailsManager(){
        return  new InMemoryUserDetailsManager(
                User.builder().username("admin").password(passwordEncoder.encode("123")).build(),
                User.builder().username("moderator").password(passwordEncoder.encode("123")).build(),
                User.builder().username("operator").password(passwordEncoder.encode("123")).build()
        );
    }

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChain(HttpSecurity htttp){

        htttp
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return htttp.build();
    }
}
