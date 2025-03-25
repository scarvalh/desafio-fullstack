package com.accenture.challenge.fullStackChallenge.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "empresa")
public class EmpresaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "cnpj")
    private String CNPJ;

    @Column(name = "cep")
    private String CEP;


    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "empresa_fornecedor",
            joinColumns = @JoinColumn(name = "id_empresa"),
            inverseJoinColumns = @JoinColumn(name = "id_fornecedor"))
    Set<FornecedorEntity> fornecedores;

    public EmpresaEntity(Integer id, String nome, String CNPJ, String CEP, Set<FornecedorEntity> fornecedores) {
        this.id = id;
        this.nome = nome;
        this.CNPJ = CNPJ;
        this.CEP = CEP;
        this.fornecedores = fornecedores;
    }

    public EmpresaEntity() {
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

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public Set<FornecedorEntity> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(Set<FornecedorEntity> fornecedores) {
        this.fornecedores = fornecedores;
    }
}
