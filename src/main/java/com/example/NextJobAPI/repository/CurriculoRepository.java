package com.example.NextJobAPI.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.NextJobAPI.model.Curriculo;
import com.example.NextJobAPI.model.Usuario;

@Repository
public interface CurriculoRepository extends JpaRepository<Curriculo, Long> {
    
    Page<Curriculo> findByUsuario(Usuario usuario, Pageable pageable);
    
    Page<Curriculo> findByUsuarioEmail(String email, Pageable pageable);
}
