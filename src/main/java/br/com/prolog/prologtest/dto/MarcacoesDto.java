package br.com.prolog.prologtest.dto;

import br.com.prolog.prologtest.util.DataUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class MarcacoesDto {

    public Integer codigoTipoMarcacao;
    public LocalDateTime dataInicial;
    public LocalDateTime dataFinal;

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dataInicialFormat = "";
        if (dataInicial != null)
            dataInicialFormat = simpleDateFormat.format(DataUtil.localDateTimeToDate(dataInicial));
        String dataFinalFormat1 = "";
        if (dataFinal != null)
            dataFinalFormat1 = simpleDateFormat.format(DataUtil.localDateTimeToDate(dataFinal));
        return "\n" +
                "codigoTipoMarcacao: " + codigoTipoMarcacao +
                "\n |Inicio=" + dataInicialFormat +"|"+
                "\n |Fim=" + dataFinalFormat1 +"|"+
                '}';
    }
}
