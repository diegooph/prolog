package br.com.prolog.prologtest.dao;

import br.com.prolog.prologtest.entity.Marcacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface MarcacaoDao extends JpaRepository<Marcacao, Long> {

    List<Marcacao> findAllByColaborador_CpfAndDataMarcacaoBetweenOrderByDataMarcacao(String cpf, Date init, Date end);
}