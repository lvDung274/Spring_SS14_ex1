package com.example.demo_employee.security;

import com.example.demo_employee.security.principal.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MyUserDetailsService userDetailsService;

    // bean passwordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // bean securityfilterchain (lõi security)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        return http
                //Tắt CSRF vì xây dựng REST API stateless
                .csrf(AbstractHttpConfigurer ::disable)
                //Không lưu trạng thái session trên server
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Cấu hình phân quyền cho các HTTP request
                .authorizeHttpRequests(
                        url->url.requestMatchers("/api/v1/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated()

                )
                .build();
    }
}
