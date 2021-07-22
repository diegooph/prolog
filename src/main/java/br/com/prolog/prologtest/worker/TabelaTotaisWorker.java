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
import br.com.prolog.prologtest.entity.MarcacaoTipo;
import br.com.prolog.prologtest.entity.TipoMarcacaoEnum;
import br.com.prolog.prologtest.util.DataUtil;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TabelaTotaisWorker {

    private final ColaboradorDao colaboradorDao;
    private final MarcacaoDao marcacaoDao;
    private final MarcacaoTipoDao marcacaoTipoDao;
    private final MarcacaoVinculoInicioFimDao marcacaoVinculoInicioFimDao;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final static int inicioHoraNoturna = 22;
    private final static int limiteHoraNoturna = 5;
    private final static int maxHoraExtraPorDia = 7;


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
            if (marcacaoInicio.getMarcacaoVinculoInicio() == null) continue;
            Marcacao marcacaoFim = marcacaoInicio.getMarcacaoVinculoInicio().getMarcacaoFim();
            MarcacoesDto marcacoesDto = new MarcacoesDto();
            if (marcacaoFim != null) {
                marcacoesDeFim.removeIf(m -> m.getCodigo() == marcacaoFim.getCodigo());
                marcacoesDto.dataFinal = DataUtil.dateToLocalDateTime(marcacaoFim.getDataMarcacao());
            }
            marcacoesDto.codigoTipoMarcacao = Math.toIntExact(marcacaoInicio.getMarcacaoTipo().getCodigo());
            marcacoesDto.dataInicial = DataUtil.dateToLocalDateTime(marcacaoInicio.getDataMarcacao());
            listaMarcacoesDto.add(marcacoesDto);
        }
        for (Marcacao marcacaoFinal : marcacoesDeFim) {
            MarcacoesDto marcacoesDto = new MarcacoesDto();
            Marcacao marcacaoInicio = marcacaoFinal.getMarcacaoVinculoFinal().getMarcacaoInicio();
            if (marcacaoInicio != null){
                marcacoesDto.dataInicial = DataUtil.dateToLocalDateTime(marcacaoInicio.getDataMarcacao());
                marcacoesDeInicio.add(marcacaoInicio);
            }
            marcacoesDto.dataFinal = DataUtil.dateToLocalDateTime(marcacaoFinal.getDataMarcacao());
            marcacoesDto.codigoTipoMarcacao = Math.toIntExact(marcacaoFinal.getCodigo());
            listaMarcacoesDto.add(marcacoesDto);
        }

        relatorioDto.listaMarcacoesDto = listaMarcacoesDto;

        List<MarcacaoTipo> marcacaoTipos = marcacoesDeInicio.stream().map(Marcacao::getMarcacaoTipo).distinct()
                .collect(Collectors.toList());

        List<TabelaTotalDto> tabelaTotalDtoList = new ArrayList<>();
        for (MarcacaoTipo marcacaoTipo : marcacaoTipos) {
            TabelaTotalDto tabelaTotalDto = new TabelaTotalDto();

            long diferencaTotalEmMinutos = marcacoesDeInicio.stream().filter(m -> m.getMarcacaoTipo().equals(marcacaoTipo)).mapToLong(m -> {
                Marcacao marcacaoFim = m.getMarcacaoVinculoInicio().getMarcacaoFim();
                if (marcacaoFim == null) return 0;
                return DataUtil.dateToLocalDateTime(m.getDataMarcacao())
                        .until(DataUtil.dateToLocalDateTime(marcacaoFim.getDataMarcacao()), ChronoUnit.MINUTES);
            }).sum();

            long adicionalNoturnoTotalEmMinutos = marcacoesDeInicio.stream().filter(m -> m.getMarcacaoTipo().equals(marcacaoTipo)).mapToLong(m -> {
                Marcacao marcacaoFim = m.getMarcacaoVinculoInicio().getMarcacaoFim();
                if (marcacaoFim == null) return 0;
                LocalDateTime initTime = DataUtil.dateToLocalDateTime(m.getDataMarcacao());
                LocalDateTime endTime = DataUtil.dateToLocalDateTime(marcacaoFim.getDataMarcacao());
                return horaNoturnaPorPeriodo(initTime, endTime);
            }).sum();


            tabelaTotalDto.codigoMarcacaoTipo = Math.toIntExact(marcacaoTipo.getCodigo());
            tabelaTotalDto.horasNoturnasClt = DataUtil.showDuration(LocalDateTime.now(), LocalDateTime.now().plusMinutes(adicionalNoturnoTotalEmMinutos));
            tabelaTotalDto.periodoTotal = DataUtil.showDuration(LocalDateTime.now(), LocalDateTime.now().plusMinutes(diferencaTotalEmMinutos));
            tabelaTotalDto.tempoRecomendado = DataUtil.showDuration(LocalDateTime.now(), LocalDateTime.now()
                    .plusMinutes(marcacaoTipo.getTempoRecomendadoMinutos())) + "";
            tabelaTotalDto.descricao = marcacaoTipo.getNome();
            tabelaTotalDtoList.add(tabelaTotalDto);
        }
        relatorioDto.tabelaTotalDto = tabelaTotalDtoList;

        return relatorioDto;
    }

    public static long horaNoturnaPorPeriodo(LocalDateTime initTime, LocalDateTime endTime) {
        if (isHorarioNormal(initTime) && isHorarioNormal(endTime) && endTime.getDayOfYear() == initTime.getDayOfYear())
            return 0;
        long diasTrabalhados = ChronoUnit.DAYS.between(initTime, endTime);
        long minutosAdicionalNoturno = 0;

        for (int i = 1; i <= diasTrabalhados; i++) {
            minutosAdicionalNoturno += maxHoraExtraPorDia * 60;
            initTime = initTime.plusDays(1);
        }

        long minutoRestanteJornada = ChronoUnit.MINUTES.between(initTime, endTime);
        if (!isHorarioNormal(initTime) && !isHorarioNormal(endTime) && minutoRestanteJornada <= 7 * 60) {
            minutosAdicionalNoturno += minutoRestanteJornada;
            return minutosAdicionalNoturno;
        }
        if (isHorarioNormal(initTime) && isHorarioNormal(endTime) && initTime.getDayOfMonth() == endTime.getDayOfMonth() + 1) {
            minutosAdicionalNoturno += 7 * 60;
            return minutosAdicionalNoturno;
        }
        LocalDateTime tempoLimiteInicio;
        if (isHorarioNormal(initTime)) {
            tempoLimiteInicio = initTime.withHour(inicioHoraNoturna).withMinute(0);
        } else {
            tempoLimiteInicio = initTime;
        }
        LocalDateTime tempoLimitefinal;
        if (isHorarioNormal(endTime)) {
            tempoLimitefinal = endTime.withHour(limiteHoraNoturna).withMinute(0);
        } else {
            tempoLimitefinal = endTime;
        }
        long minutosRestantes = ChronoUnit.MINUTES.between(tempoLimiteInicio, tempoLimitefinal);
        int tempoPeriodoNormal = 17 * 60;
        if (minutosRestantes > tempoPeriodoNormal) minutosRestantes -= tempoPeriodoNormal;
        minutosAdicionalNoturno += minutosRestantes;


        return minutosAdicionalNoturno;
    }

    public static boolean isHorarioNormal(LocalDateTime localDateTime) {
        return localDateTime.isAfter(localDateTime.withHour(limiteHoraNoturna).withMinute(1))
                && localDateTime.isBefore(localDateTime.withHour(inicioHoraNoturna).withMinute(1));

    }

    public static void main(String[] args) {
        LocalDateTime initDate = LocalDateTime.of(2019, 3, 10, 3, 5);
        LocalDateTime endDate = LocalDateTime.of(2019, 3, 11, 2, 58);

        System.out.println(
                isHorarioNormal(initDate));
        System.out.println(
                isHorarioNormal(endDate));
        System.out.println(
                horaNoturnaPorPeriodo(initDate, endDate)
        );
        System.out.println(initDate);
        System.out.println(LocalDateTime.now().until(LocalDateTime.now().plusMinutes(5549526), ChronoUnit.MINUTES));
    }

}
