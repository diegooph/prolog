package br.com.prolog.prologtest.dto;

public class TabelaTotalDto {

    public Integer codigoMarcacaoTipo;
    public String descricao;
    public String tempoRecomendado;
    public String periodoTotal;
    public String horasNoturnasClt;

    @Override
    public String toString() {
        return "\n\nTabelaTotalDto{" +
                " \n codigoMarcacaoTipo=" + codigoMarcacaoTipo +
                " \n descricao='" + descricao + '\'' +
                " \n tempoRecomendado='" + tempoRecomendado + '\'' +
                " \n periodoTotal='" + periodoTotal + '\'' +
                " \n horasNoturnasClt='" + horasNoturnasClt + '\'' +
                '}';
    }
}
