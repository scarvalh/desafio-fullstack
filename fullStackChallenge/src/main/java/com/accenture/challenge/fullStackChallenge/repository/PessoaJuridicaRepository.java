package com.accenture.challenge.fullStackChallenge.repository;

import com.accenture.challenge.fullStackChallenge.entity.PessoaJuridicaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridicaEntity, Integer> {
    Optional<PessoaJuridicaEntity> findByCNPJ(String cnpj);
}
