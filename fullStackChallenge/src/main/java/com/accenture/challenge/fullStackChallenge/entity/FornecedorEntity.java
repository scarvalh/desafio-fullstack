package com.accenture.challenge.fullStackChallenge.entity;

import com.accenture.challenge.fullStackChallenge.dto.FornecedorDTO;
import com.accenture.challenge.fullStackChallenge.enums.TiposFornecedor;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "fornecedor")
public class FornecedorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fornecedor")
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "CEP")
    private String CEP;

    @Column(name = "tipo", columnDefinition = "char(2)")
    @Enumerated(EnumType.STRING)
    private TiposFornecedor tipo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pessoa_fisica", referencedColumnName = "id_pessoa_fisica")
    private PessoaFisicaEntity pessoaFisica;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_pessoa_juridica", referencedColumnName = "id_pessoa_juridica  ")
    private PessoaJuridicaEntity pessoaJuridica;


    @ManyToMany(mappedBy = "fornecedores")
    Set<EmpresaEntity> empresas;

    public FornecedorEntity(Integer id, String email, String CEP, TiposFornecedor tipo, PessoaFisicaEntity pessoaFisica, PessoaJuridicaEntity pessoaJuridica) {
        this.id = id;
        this.email = email;
        this.CEP = CEP;
        this.tipo = tipo;
        this.pessoaFisica = pessoaFisica;
        this.pessoaJuridica = pessoaJuridica;
    }

    public FornecedorEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCEP() {
        return CEP;
    }

    public void setCEP(String CEP) {
        this.CEP = CEP;
    }

    public TiposFornecedor getTipo() {
        return tipo;
    }

    public void setTipo(TiposFornecedor tipo) {
        this.tipo = tipo;
    }

    public PessoaFisicaEntity getPessoaFisica() {
        return pessoaFisica;
    }

    public void setPessoaFisica(PessoaFisicaEntity pessoaFisica) {
        this.pessoaFisica = pessoaFisica;
    }

    public PessoaJuridicaEntity getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(PessoaJuridicaEntity pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public Set<EmpresaEntity> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(Set<EmpresaEntity> empresas) {
        this.empresas = empresas;
    }
}
