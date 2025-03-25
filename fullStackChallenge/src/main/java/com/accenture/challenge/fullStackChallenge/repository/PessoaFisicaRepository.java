package com.accenture.challenge.fullStackChallenge.repository;

import com.accenture.challenge.fullStackChallenge.entity.PessoaFisicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisicaEntity, Integer> {
    Optional<PessoaFisicaEntity> findByCPF(String cpf);
}
