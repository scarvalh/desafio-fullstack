package com.accenture.challenge.fullStackChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pessoa_juridica")
public class PessoaJuridicaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa_juridica")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cnpj")
    private String CNPJ;

    public PessoaJuridicaEntity(Integer id, String nome, String CNPJ) {
        this.id = id;
        this.nome = nome;
        this.CNPJ = CNPJ;
    }

    public PessoaJuridicaEntity() {
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
