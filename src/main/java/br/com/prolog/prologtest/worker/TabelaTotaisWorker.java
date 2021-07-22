package br.com.prolog.prologtest.worker;

import br.com.prolog.prologtest.dao.ColaboradorDao;
import br.com.prolog.prologtest.dao.MarcacaoDao;
import br.com.prolog.prologtest.dao.MarcacaoTipoDao;
import br.com.prolog.prologtest.dao.MarcacaoVinculoInicioFimDao;
import br.com.prolog.prologtest.dto.MarcacoesDto;
import br.com.prolog.prologtest.dto.RelatorioDto;
import br.com.prolog.prologtest.dto.TabelaTotalDto;
import br.com.prolog.prologtest.entity.Colaborador;
import br.com.prolog.prologtest.entity.Marcacao;
import br.com.prolog.prologtest.entity.TipoMarcacaoEnum;
import br.com.prolog.prologtest.util.DataUtil;

import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TabelaTotaisWorker {

    private final ColaboradorDao colaboradorDao;
    private final MarcacaoDao marcacaoDao;
    private final MarcacaoTipoDao marcacaoTipoDao;
    private final MarcacaoVinculoInicioFimDao marcacaoVinculoInicioFimDao;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public TabelaTotaisWorker(ColaboradorDao colaboradorDao, MarcacaoDao marcacaoDao, MarcacaoTipoDao marcacaoTipoDao, MarcacaoVinculoInicioFimDao marcacaoVinculoInicioFimDao) {
        this.colaboradorDao = colaboradorDao;
        this.marcacaoDao = marcacaoDao;
        this.marcacaoTipoDao = marcacaoTipoDao;
        this.marcacaoVinculoInicioFimDao = marcacaoVinculoInicioFimDao;
    }

    public void printRelatorioDto(String nome, LocalDateTime initDate, LocalDateTime endDate) {
        System.out.println(gerarRelatorioColaborador(nome, initDate, endDate).print());
    }

    public RelatorioDto gerarRelatorioColaborador(String nome, LocalDateTime initDate, LocalDateTime endDate) {
        Colaborador colaborador = colaboradorDao.getColaboradorByNome(nome);
        List<Marcacao> marcacoesColaborador =
                marcacaoDao.findAllByColaborador_CpfAndDataMarcacaoBetweenOrderByDataMarcacao(colaborador.getCpf(),
                        DataUtil.localDateTimeToDate(initDate), DataUtil.localDateTimeToDate(endDate));

        return gerarRelatorioPorListaMarcacaoDoColaborador(marcacoesColaborador, colaborador);
    }

    private RelatorioDto gerarRelatorioPorListaMarcacaoDoColaborador(List<Marcacao> marcacoesColaborador, Colaborador colaborador) {
        RelatorioDto relatorioDto = new RelatorioDto();
        relatorioDto.nomeColaborador = colaborador.getNome();

        List<Marcacao> marcacoesDeInicio = marcacoesColaborador.stream().filter(m ->
                m.getTipoMarcacaoEnum().equals(TipoMarcacaoEnum.MARCACAO_INICIO))
                .collect(Collectors.toList());
        List<Marcacao> marcacoesDeFim = marcacoesColaborador.stream().filter(m ->
                m.getTipoMarcacaoEnum().equals(TipoMarcacaoEnum.MARCACAO_FIM))
                .collect(Collectors.toList());



        List<MarcacoesDto> listaMarcacoesDto = new ArrayList<>();
        for (Marcacao marcacaoInicio : marcacoesDeInicio) {
            if (marcacaoInicio.getMarcacaoVinculoInicio() == null)continue;
            Marcacao marcacaoFim = marcacaoInicio.getMarcacaoVinculoInicio().getMarcacaoFim();
            MarcacoesDto marcacoesDto = new MarcacoesDto();
            if (marcacaoFim != null){
                marcacoesDeFim.removeIf(m->m.getCodigo() == marcacaoFim.getCodigo());
                marcacoesDto.dataFinal = DataUtil.dateToLocalDate(marcacaoFim.getDataMarcacao());
            }
            marcacoesDto.codigoTipoMarcacao = Math.toIntExact(marcacaoInicio.getMarcacaoTipo().getCodigo());
            marcacoesDto.dataInicial = DataUtil.dateToLocalDate(marcacaoInicio.getDataMarcacao());
            listaMarcacoesDto.add(marcacoesDto);
        }
        for (Marcacao marcacaoFinal : marcacoesDeFim) {
            MarcacoesDto marcacoesDto = new MarcacoesDto();
            marcacoesDto.dataFinal = DataUtil.dateToLocalDate(marcacaoFinal.getDataMarcacao());
            marcacoesDto.codigoTipoMarcacao = Math.toIntExact(marcacaoFinal.getCodigo());

            listaMarcacoesDto.add(marcacoesDto);
        }
        relatorioDto.listaMarcacoesDto = listaMarcacoesDto;

        TabelaTotalDto tabelaTotalDto = new TabelaTotalDto();
        tabelaTotalDto.codigoMarcacaoTipo = 1;
        tabelaTotalDto.horasNoturnasClt = "";
        tabelaTotalDto.periodoTotal = "";
        tabelaTotalDto.tempoRecomendado = "";
        tabelaTotalDto.descricao = "";

        relatorioDto.tabelaTotalDto = tabelaTotalDto;
        return relatorioDto;
    }



}
