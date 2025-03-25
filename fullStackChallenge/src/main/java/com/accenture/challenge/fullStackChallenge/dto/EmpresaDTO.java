package com.accenture.challenge.fullStackChallenge.dto;

import com.accenture.challenge.fullStackChallenge.entity.EmpresaEntity;

import java.util.List;
import java.util.stream.Collectors;

public class EmpresaDTO {
    private Integer id;
    private String nome;
    private String cnpj;
    private String cep;
    private List<FornecedorDTO> fornecedores;

    public EmpresaDTO(Integer id, String nome, String cnpj, String cep, List<FornecedorDTO> fornecedores) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.cep = cep;
        this.fornecedores = fornecedores;
    }

    public EmpresaDTO() {
    }

    public EmpresaDTO(EmpresaEntity entity){
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.cnpj = entity.getCNPJ();
        this.cep = entity.getCEP();
        this.fornecedores = entity.getFornecedores().stream().map(FornecedorDTO::new).collect(Collectors.toList());
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public List<FornecedorDTO> getFornecedores() {
        return fornecedores;
    }

    public void setFornecedores(List<FornecedorDTO> fornecedores) {
        this.fornecedores = fornecedores;
    }
}
