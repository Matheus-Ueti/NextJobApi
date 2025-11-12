package com.example.NextJobAPI.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.NextJobAPI.model.Analise;
import com.example.NextJobAPI.model.Curriculo;

@Repository
public interface AnaliseRepository extends JpaRepository<Analise, Long> {
    
    Optional<Analise> findByCurriculo(Curriculo curriculo);
    
    Optional<Analise> findByCurriculoId(Long curriculoId);
}
