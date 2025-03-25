package com.accenture.challenge.fullStackChallenge.dto;

import com.accenture.challenge.fullStackChallenge.entity.FornecedorEntity;
import com.accenture.challenge.fullStackChallenge.enums.TiposFornecedor;

import java.util.Date;

public class FornecedorDTO {

    private Integer id;
    private String email;
    private String CEP;
    private String tipo;
    private String nome;
    private String CNPJ;
    private String CPF;
    private String RG;
    private Date dataNascimento;

    public FornecedorDTO(
            Integer id,
            String email,
            String CEP,
            String tipo,
            String nome,
            String CNPJ,
            String CPF,
            String RG,
            Date dataNascimento) {
        this.id = id;
        this.email = email;
        this.CEP = CEP;
        this.tipo = tipo;
        this.nome = nome;
        this.CNPJ = CNPJ;
        this.CPF = CPF;
        this.RG = RG;
        this.dataNascimento = dataNascimento;
    }

    public FornecedorDTO(FornecedorEntity entity) {
        this.id = entity.getId();
        this.email = entity.getEmail();
        this.CEP = entity.getCEP();
        this.tipo = entity.getTipo().getValue();

        if(TiposFornecedor.PF.getValue().equals(tipo)){
            this.nome = entity.getPessoaFisica().getNome();
            this.CPF = entity.getPessoaFisica().getCPF();
            this.RG = entity.getPessoaFisica().getRG();
            this.dataNascimento = entity.getPessoaFisica().getDataDeNascimento();
        } else {
            this.CNPJ = entity.getPessoaJuridica().getCNPJ();
            this.nome = entity.getPessoaJuridica().getNome();
        }

    }

    public FornecedorDTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getRG() {
        return RG;
    }

    public void setRG(String RG) {
        this.RG = RG;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getCNPJ() {
        return CNPJ;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
