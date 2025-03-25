package com.accenture.challenge.fullStackChallenge.enums;

import java.util.Arrays;

public enum TiposFornecedor {
    PF("Pessoa Fisica"),
    PJ("Pessoa Juridica");

    private String descricao;

    TiposFornecedor(String descricao) {
        this.descricao = descricao;
    }

    public static TiposFornecedor valueOfEnum(String descricao){
        return Arrays.stream(TiposFornecedor.values()).filter(v -> v.getValue().equals(descricao))
                .findAny().orElseThrow();
    }

    public String getValue() {
        return descricao;
    }
}
