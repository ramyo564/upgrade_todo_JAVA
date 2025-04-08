package com.example.todo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 비활성화
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::sameOrigin) // H2 콘솔 iframe 허용
                .xssProtection(withDefaults())
                .contentSecurityPolicy(csp ->
                    csp.policyDirectives(
                        "default-src 'self'; "
                            + "frame-ancestors 'self'; "
                            + "script-src 'self' 'unsafe-inline' "
                            + "'unsafe-eval';"
                            + " style-src 'self' 'unsafe-inline';")
                )
            )
            // 요청 권한 설정
            .authorizeHttpRequests(authorize -> authorize
                // Swagger UI 관련 경로 허용
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/api-docs/**",
                    "/swagger-resources/**"
                ).permitAll()
                // H2 콘솔 관련 경로 허용
                .requestMatchers(
                    new AntPathRequestMatcher(
                    "/h2-console/**")
                )
                .permitAll()
                // 회원가입 및 로그인 관련 경로 허용
                .requestMatchers(
                    "/api/users", "/api/users/login")
                .permitAll()
                .requestMatchers(
                    "/favicon.ico")
                .permitAll()
                // 나머지 모든 요청은 인증 필요
                .anyRequest().authenticated()
            )
            // H2 콘솔을 위한 설정
            .headers(headers -> headers
                .frameOptions(FrameOptionsConfig::disable)
            )
            // 기본 로그인 폼 비활성화
            .formLogin(AbstractHttpConfigurer::disable)
            // HTTP Basic 인증 비활성화
            .httpBasic(AbstractHttpConfigurer::disable);
        
        return http.build();
    }
} 