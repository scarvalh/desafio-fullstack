package com.accenture.challenge.fullStackChallenge.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "pessoa_fisica")
public class PessoaFisicaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pessoa_fisica")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cpf")
    private String CPF;

    @Column(name = "rg")
    private String RG;

    @Column(name = "data_nascimento")
    private Date dataDeNascimento;

    public PessoaFisicaEntity(Integer id, Date dataDeNascimento, String RG, String CPF, String nome) {
        this.id = id;
        this.dataDeNascimento = dataDeNascimento;
        this.RG = RG;
        this.CPF = CPF;
        this.nome = nome;
    }

    public PessoaFisicaEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public Date getDataDeNascimento() {
        return dataDeNascimento;
    }

    public void setDataDeNascimento(Date dataDeNascimento) {
        this.dataDeNascimento = dataDeNascimento;
    }
}
