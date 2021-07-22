package br.com.prolog.prologtest.dao;

import br.com.prolog.prologtest.entity.MarcacaoVinculoInicioFim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcacaoVinculoInicioFimDao extends JpaRepository<MarcacaoVinculoInicioFim, Long> {
}