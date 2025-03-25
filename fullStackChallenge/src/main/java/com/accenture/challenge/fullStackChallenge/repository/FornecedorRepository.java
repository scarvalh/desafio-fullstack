package com.accenture.challenge.fullStackChallenge.repository;

import com.accenture.challenge.fullStackChallenge.entity.FornecedorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FornecedorRepository extends JpaRepository<FornecedorEntity, Integer> {
}
