package com.example.NextJobAPI.service;

import com.example.NextJobAPI.exception.ValidationException;
import com.example.NextJobAPI.model.Usuario;
import com.example.NextJobAPI.repository.UsuarioRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UsuarioService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UsuarioRepository usuarioRepository;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        register(oAuth2User);
        return oAuth2User;
    }    @Transactional
    public Usuario register(OAuth2User oAuth2User) {
        // GitHub pode retornar email como null se não for público
        // Primeiro tenta pegar email, senão usa login@github.com
        final String email;
        String emailAttr = attributeToString(oAuth2User, "email");
        if (emailAttr == null || emailAttr.isBlank()) {
            String login = attributeToString(oAuth2User, "login");
            if (login != null && !login.isBlank()) {
                email = login + "@github.com";
            } else {
                throw new ValidationException("OAuth2 provider não retornou email ou login");
            }
        } else {
            email = emailAttr;
        }

        return usuarioRepository.findByEmail(email)
                .map(existing -> updateUser(existing, oAuth2User))
                .orElseGet(() -> createUser(email, oAuth2User));
    }

    private Usuario createUser(String email, OAuth2User oAuth2User) {
        Usuario usuario = Usuario.builder()
                .email(email)
                .nome(resolveName(oAuth2User, email))
                .fotoUrl(resolvePicture(oAuth2User))
                .build();
        return usuarioRepository.save(usuario);
    }

    private Usuario updateUser(Usuario existing, OAuth2User oAuth2User) {
        String nome = resolveName(oAuth2User, existing.getEmail());
        String fotoUrl = resolvePicture(oAuth2User);
        boolean changed = false;

        if (nome != null && !nome.equals(existing.getNome())) {
            existing.setNome(nome);
            changed = true;
        }
        if (fotoUrl != null && !fotoUrl.equals(existing.getFotoUrl())) {
            existing.setFotoUrl(fotoUrl);
            changed = true;
        }

        return changed ? usuarioRepository.save(existing) : existing;
    }

    private String resolveName(OAuth2User oAuth2User, String emailFallback) {
        String name = attributeToString(oAuth2User, "name");
        if (name == null || name.isBlank()) {
            name = attributeToString(oAuth2User, "given_name");
        }
        if (name == null || name.isBlank()) {
            int atIndex = emailFallback.indexOf('@');
            return atIndex > 0 ? emailFallback.substring(0, atIndex) : emailFallback;
        }
        return name;
    }

    private String resolvePicture(OAuth2User oAuth2User) {
        String picture = attributeToString(oAuth2User, "picture");
        if (picture == null || picture.isBlank()) {
            picture = attributeToString(oAuth2User, "avatar_url");
        }
        return picture;
    }

    private String attributeToString(OAuth2User oAuth2User, String attributeName) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        Object value = attributes.get(attributeName);
        return value != null ? value.toString() : null;
    }
    
    @Transactional
    public Usuario buscarOuCriarUsuario(String email, String nome) {
        return usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novoUsuario = Usuario.builder()
                            .email(email)
                            .nome(nome)
                            .build();
                    return usuarioRepository.save(novoUsuario);
                });
    }
}
