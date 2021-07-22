package br.com.prolog.prologtest.dto;

import java.util.List;

public class RelatorioDto {
    public String nomeColaborador;
    public List<String> diasIniciaisMarcacoes;
    public List<MarcacoesDto> listaMarcacoesDto;
    public TabelaTotalDto tabelaTotalDto;

    public String print() {
        StringBuilder print = new StringBuilder("RelatorioDto" +
                "\n\n" +
                "Colaborador: " + nomeColaborador + "\n\n");
        for (String diasIniciaisMarcacoe : diasIniciaisMarcacoes) {
            print.append(diasIniciaisMarcacoe).append(": ").append(listaMarcacoesDto);
        }


        return print.toString();
    }

}
