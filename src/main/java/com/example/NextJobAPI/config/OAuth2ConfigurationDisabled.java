package com.example.NextJobAPI.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;

/**
 * OAuth2 Configuration - DESABILITADO TEMPORARIAMENTE
 * 
 * Para habilitar:
 * 1. Configure o Google Cloud Console
 * 2. Obtenha Client ID e Secret válidos
 * 3. Atualize application.properties
 * 4. Remova a anotação @ConditionalOnProperty
 */
@Configuration
@ConditionalOnProperty(name = "oauth2.enabled", havingValue = "true", matchIfMissing = false)
public class OAuth2ConfigurationDisabled {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        // Bean vazio para evitar erros quando OAuth2 está desabilitado
        return new InMemoryClientRegistrationRepository();
    }
}
