package com.example.NextJobAPI.service;

import com.example.NextJobAPI.dto.CurriculoRequestDTO;
import com.example.NextJobAPI.dto.CurriculoResponseDTO;
import com.example.NextJobAPI.exception.BusinessException;
import com.example.NextJobAPI.exception.ResourceNotFoundException;
import com.example.NextJobAPI.model.Curriculo;
import com.example.NextJobAPI.model.Usuario;
import com.example.NextJobAPI.repository.CurriculoRepository;
import com.example.NextJobAPI.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CurriculoService {
    
    private final CurriculoRepository curriculoRepository;
    private final UsuarioRepository usuarioRepository;
    
    @Transactional
    @CacheEvict(value = "curriculos", allEntries = true)
    public CurriculoResponseDTO criar(CurriculoRequestDTO request, String userEmail) {
        log.info("Criando currículo para usuário: {}", userEmail);
        
        Usuario usuario = usuarioRepository.findByEmail(userEmail)
                .orElseGet(() -> criarNovoUsuario(userEmail));
        
        Curriculo curriculo = Curriculo.builder()
                .usuario(usuario)
                .nome(request.getNome())
                .cargoAtual(request.getCargoAtual())
                .cargoDesejado(request.getCargoDesejado())
                .habilidades(request.getHabilidades())
                .experiencia(request.getExperiencia())
                .educacao(request.getEducacao())
                .build();
        
        curriculo = curriculoRepository.save(curriculo);
        
        return mapToResponseDTO(curriculo);
    }
    
    @Transactional(readOnly = true)
    @Cacheable(value = "curriculos", key = "#id")
    public CurriculoResponseDTO buscarPorId(Long id, String userEmail) {
        log.info("Buscando currículo ID: {} para usuário: {}", id, userEmail);
        
        Curriculo curriculo = curriculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", id));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para acessar este currículo");
        }
        
        return mapToResponseDTO(curriculo);
    }
    
    @Transactional(readOnly = true)
    public Page<CurriculoResponseDTO> listarPorUsuario(String userEmail, Pageable pageable) {
        log.info("Listando currículos do usuário: {}", userEmail);
        
        return curriculoRepository.findByUsuarioEmail(userEmail, pageable)
                .map(this::mapToResponseDTO);
    }
    
    @Transactional
    @CacheEvict(value = "curriculos", allEntries = true)
    public CurriculoResponseDTO atualizar(Long id, CurriculoRequestDTO request, String userEmail) {
        log.info("Atualizando currículo ID: {}", id);
        
        Curriculo curriculo = curriculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", id));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para atualizar este currículo");
        }
        
        curriculo.setNome(request.getNome());
        curriculo.setCargoAtual(request.getCargoAtual());
        curriculo.setCargoDesejado(request.getCargoDesejado());
        curriculo.setHabilidades(request.getHabilidades());
        curriculo.setExperiencia(request.getExperiencia());
        curriculo.setEducacao(request.getEducacao());
        
        curriculo = curriculoRepository.save(curriculo);
        
        return mapToResponseDTO(curriculo);
    }
    
    @Transactional
    @CacheEvict(value = "curriculos", allEntries = true)
    public void deletar(Long id, String userEmail) {
        log.info("Deletando currículo ID: {}", id);
        
        Curriculo curriculo = curriculoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Currículo", "id", id));
        
        if (!curriculo.getUsuario().getEmail().equals(userEmail)) {
            throw new BusinessException("Você não tem permissão para deletar este currículo");
        }
        
        curriculoRepository.delete(curriculo);
    }
    
    private Usuario criarNovoUsuario(String email) {
        Usuario usuario = Usuario.builder()
                .email(email)
                .nome(email.split("@")[0])
                .build();
        
        return usuarioRepository.save(usuario);
    }
    
    private CurriculoResponseDTO mapToResponseDTO(Curriculo curriculo) {
        return CurriculoResponseDTO.builder()
                .id(curriculo.getId())
                .nome(curriculo.getNome())
                .cargoAtual(curriculo.getCargoAtual())
                .cargoDesejado(curriculo.getCargoDesejado())
                .habilidades(curriculo.getHabilidades())
                .experiencia(curriculo.getExperiencia())
                .educacao(curriculo.getEducacao())
                .pdfUrl(curriculo.getPdfUrl())
                .criadoEm(curriculo.getCriadoEm())
                .atualizadoEm(curriculo.getAtualizadoEm())
                .build();
    }
}
