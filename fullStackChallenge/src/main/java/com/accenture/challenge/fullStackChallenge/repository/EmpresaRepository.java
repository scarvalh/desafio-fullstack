package com.accenture.challenge.fullStackChallenge.repository;

import com.accenture.challenge.fullStackChallenge.entity.EmpresaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<EmpresaEntity, Integer> {
    Optional<EmpresaEntity> findByCNPJ(String cnpj);
}
