package com.accenture.challenge.fullStackChallenge.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "empresa_fornecedor")
public class EmpresaFornecedorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_empresa")
    private Integer idEmpresa;

    @Column(name = "id_fornecedor")
    private Integer idFornecedor;

    public EmpresaFornecedorEntity(Integer id, Integer idEmpresa, Integer idFornecedor) {
        this.id = id;
        this.idEmpresa = idEmpresa;
        this.idFornecedor = idFornecedor;
    }

    public EmpresaFornecedorEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Integer getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(Integer idFornecedor) {
        this.idFornecedor = idFornecedor;
    }
}
