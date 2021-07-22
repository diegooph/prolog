package br.com.prolog.prologtest;

import br.com.prolog.prologtest.dao.ColaboradorDao;
import br.com.prolog.prologtest.dao.MarcacaoDao;
import br.com.prolog.prologtest.dao.MarcacaoTipoDao;
import br.com.prolog.prologtest.dao.MarcacaoVinculoInicioFimDao;
import br.com.prolog.prologtest.entity.Marcacao;
import br.com.prolog.prologtest.worker.TabelaTotaisWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class PrologTestApplication {
    @Autowired
    private ColaboradorDao colaboradorDao;
    @Autowired
    private MarcacaoDao marcacaoDao;
    @Autowired
    private MarcacaoTipoDao marcacaoTipoDao;
    @Autowired
    private MarcacaoVinculoInicioFimDao marcacaoVinculoInicioFimDao;

    public static void main(String[] args) {
        SpringApplication.run(PrologTestApplication.class, args);

    }

    @Bean
    CommandLineRunner runner() {
        return args -> {

            TabelaTotaisWorker tabelaTotaisWorker = new TabelaTotaisWorker(colaboradorDao,
                    marcacaoDao,
                    marcacaoTipoDao,
                    marcacaoVinculoInicioFimDao);
            LocalDateTime initDate = LocalDateTime.of(2019, 3, 1, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2019, 3, 10, 23, 59);
            tabelaTotaisWorker.printRelatorioDto("João da Silva", initDate, endDate);
        };
    }
}
