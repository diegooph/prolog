package br.com.prolog.prologtest.dto;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class MarcacoesDto {

    public Integer codigoTipoMarcacao;
    public LocalDateTime dataInicial;
    public LocalDateTime dataFinal;

    @Override
    public String toString() {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");

        return "MarcacoesDto\n" +
                "codigoTipoMarcacao: " + codigoTipoMarcacao +
                "\n |Inicio=" +  simpleDateFormat.format(dataInicial)  +"|"+
                "\n |Fim=" +  simpleDateFormat.format(dataFinal) +"|"+
                '}';
    }
}
