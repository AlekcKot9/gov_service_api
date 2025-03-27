package gov_service_api.security;

import jakarta.servlet.http.*;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.http.*;
import org.springframework.security.web.*;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Отключаем CSRF для REST API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/user/*").permitAll()
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .formLogin(login -> login.disable()) // Отключаем стандартную форму входа
                .httpBasic().disable() // Отключаем HTTP Basic для REST API
                .logout(logout -> logout
                        .logoutUrl("/auth/logout") // URL для выхода
                        .invalidateHttpSession(true) // Удаляем сессию
                        .deleteCookies("JSESSIONID") // Удаляем куки
                )
                .build();
    }

    @Bean
    public HttpSessionListener httpSessionListener() {
        return new HttpSessionListener() {
            @Override
            public void sessionCreated(HttpSessionEvent se) {
                // Noncompliant - method is empty
            }

            @Override
            public void sessionDestroyed(HttpSessionEvent se) {
                // Noncompliant - method is empty
            }
        };
    }
}