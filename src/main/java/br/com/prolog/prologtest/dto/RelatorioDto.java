package br.com.prolog.prologtest.dto;

import br.com.prolog.prologtest.util.DataUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioDto {
    public String nomeColaborador;
    public List<MarcacoesDto> listaMarcacoesDto;
    public TabelaTotalDto tabelaTotalDto;

    public String print() {
        StringBuilder print = new StringBuilder("RelatorioDto" +
                "\n\n" +
                "Colaborador: " + nomeColaborador + "\n\n");

        List<LocalDate> collectInitDates = listaMarcacoesDto.stream().map(m -> {
            if (m.dataInicial == null) {
                return m.dataFinal.toLocalDate();
            } else {
                return m.dataInicial.toLocalDate();
            }
        }).distinct()
                .collect(Collectors.toList());
        for (LocalDate collectInitDate : collectInitDates) {
            List<MarcacoesDto> listaMarcacoesDto = this.listaMarcacoesDto.stream().filter(m -> {
                if (m.dataInicial == null) {
                    return m.dataFinal.toLocalDate().isEqual(collectInitDate);
                } else {
                    return m.dataInicial.toLocalDate().isEqual(collectInitDate);
                }
            })
                    .collect(Collectors.toList());
            print.append("\n\n\nData:").append(DataUtil.formatLocalDate(collectInitDate));
            for (MarcacoesDto marcacoesDto : listaMarcacoesDto) {
                print.append(": \n").append(marcacoesDto);
            }


        }
        print.append("\n");
        print.append(tabelaTotalDto);
        return print.toString();
    }

}
