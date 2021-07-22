package br.com.prolog.prologtest.entity;

public enum TipoMarcacaoEnum {

    MARCACAO_FIM("MARCACAO_FIM"),
    MARCACAO_INICIO("MARCACAO_INICIO");

    private String descricao;

    TipoMarcacaoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.descricao;
    }

}