package com.example.NextJobAPI.repository;

import com.example.NextJobAPI.model.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Long> {
    
    List<Perfil> findByUsuarioId(Long usuarioId);
    
    Optional<Perfil> findByIdAndUsuarioId(Long id, Long usuarioId);
}
