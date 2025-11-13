package com.example.NextJobAPI.service;

import com.example.NextJobAPI.dto.PerfilRequestDTO;
import com.example.NextJobAPI.dto.PerfilResponseDTO;
import com.example.NextJobAPI.exception.ResourceNotFoundException;
import com.example.NextJobAPI.model.Perfil;
import com.example.NextJobAPI.model.Usuario;
import com.example.NextJobAPI.repository.PerfilRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PerfilService {
    
    private final PerfilRepository perfilRepository;
    private final UsuarioService usuarioService;
    
    @Transactional
    @CacheEvict(value = "perfis", allEntries = true)
    public PerfilResponseDTO criarPerfil(PerfilRequestDTO request, String email) {
        log.info("Criando perfil para usuário: {}", email);
        
        Usuario usuario = usuarioService.buscarOuCriarUsuario(email, email);
        
        Perfil perfil = new Perfil();
        perfil.setNomeCompleto(request.getNomeCompleto());
        perfil.setTelefone(request.getTelefone());
        perfil.setLinkedin(request.getLinkedin());
        perfil.setGithub(request.getGithub());
        perfil.setPortfolio(request.getPortfolio());
        perfil.setUsuario(usuario);
        
        Perfil salvo = perfilRepository.save(perfil);
        log.info("Perfil criado com ID: {}", salvo.getId());
        
        return toResponseDTO(salvo);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "perfis", key = "#email")
    public List<PerfilResponseDTO> listarPerfis(String email) {
        log.info("Listando perfis do usuário: {}", email);
        
        Usuario usuario = usuarioService.buscarOuCriarUsuario(email, email);
        
        return perfilRepository.findByUsuarioId(usuario.getId())
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "perfis", key = "#id + '-' + #email")
    public PerfilResponseDTO buscarPorId(Long id, String email) {
        log.info("Buscando perfil {} do usuário: {}", id, email);
        
        Usuario usuario = usuarioService.buscarOuCriarUsuario(email, email);
        
        Perfil perfil = perfilRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + id));
        
        return toResponseDTO(perfil);
    }
    
    @Transactional
    @CacheEvict(value = "perfis", allEntries = true)
    public PerfilResponseDTO atualizar(Long id, PerfilRequestDTO request, String email) {
        log.info("Atualizando perfil {} do usuário: {}", id, email);
        
        Usuario usuario = usuarioService.buscarOuCriarUsuario(email, email);
        
        Perfil perfil = perfilRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + id));
        
        perfil.setNomeCompleto(request.getNomeCompleto());
        perfil.setTelefone(request.getTelefone());
        perfil.setLinkedin(request.getLinkedin());
        perfil.setGithub(request.getGithub());
        perfil.setPortfolio(request.getPortfolio());
        
        Perfil atualizado = perfilRepository.save(perfil);
        log.info("Perfil {} atualizado com sucesso", id);
        
        return toResponseDTO(atualizado);
    }
    
    @Transactional
    @CacheEvict(value = "perfis", allEntries = true)
    public void deletar(Long id, String email) {
        log.info("Deletando perfil {} do usuário: {}", id, email);
        
        Usuario usuario = usuarioService.buscarOuCriarUsuario(email, email);
        
        Perfil perfil = perfilRepository.findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Perfil não encontrado com ID: " + id));
        
        perfilRepository.delete(perfil);
        log.info("Perfil {} deletado com sucesso", id);
    }
    
    private PerfilResponseDTO toResponseDTO(Perfil perfil) {
        return new PerfilResponseDTO(
                perfil.getId(),
                perfil.getNomeCompleto(),
                perfil.getTelefone(),
                perfil.getLinkedin(),
                perfil.getGithub(),
                perfil.getPortfolio(),
                perfil.getUsuario().getEmail(),
                perfil.getCriadoEm(),
                perfil.getAtualizadoEm()
        );
    }
}
