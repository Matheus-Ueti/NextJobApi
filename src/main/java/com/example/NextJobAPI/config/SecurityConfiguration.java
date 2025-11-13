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
public class SecurityConfiguration {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UsuarioService usuarioService) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()  // TEMPORÁRIO: Permitir tudo sem autenticação
                )
                // OAuth2 Login desabilitado temporariamente
                // .oauth2Login(oauth2 -> oauth2
                //     .userInfoEndpoint(userInfo -> userInfo.userService(usuarioService))
                //     .defaultSuccessUrl("/", true))
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .build();
    }
}
