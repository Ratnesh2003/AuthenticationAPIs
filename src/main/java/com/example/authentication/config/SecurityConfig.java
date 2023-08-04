package com.example.authentication.config;


import com.example.authentication.service.jwt.AuthTokenFilter;
import com.example.authentication.service.jwt.JwtAuthEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthEntryPoint jwtAuthEntryPoint;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }



}
