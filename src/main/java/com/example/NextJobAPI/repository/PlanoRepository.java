package com.example.NextJobAPI.repository;

import com.example.NextJobAPI.model.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    
    List<Plano> findByUsuarioId(Long usuarioId);
    
    Optional<Plano> findByIdAndUsuarioId(Long id, Long usuarioId);
    
    List<Plano> findByUsuarioIdAndStatus(Long usuarioId, String status);
}
