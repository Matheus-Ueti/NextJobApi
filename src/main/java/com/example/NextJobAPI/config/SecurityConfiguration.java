package com.example.NextJobAPI.config;

import com.example.NextJobAPI.service.UsuarioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/login", "/h2-console/**", "/error", "/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                    .anyRequest().authenticated()
                )
                // OAuth2 Login ATIVADO - Usa página customizada
                .oauth2Login(oauth2 -> oauth2
                    .loginPage("/login")
                    .userInfoEndpoint(userInfo -> userInfo.userService(usuarioService))
                    .defaultSuccessUrl("/home", true)
                    .failureHandler((request, response, exception) -> {
                        // Log do erro para debug
                        System.err.println("Erro no OAuth2 Login: " + exception.getMessage());
                        exception.printStackTrace();
                        response.sendRedirect("/login?error=credentials");
                    }))
                .formLogin(form -> form.disable())
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }
}
