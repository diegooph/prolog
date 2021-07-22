package br.com.prolog.prologtest.dao;

import br.com.prolog.prologtest.entity.MarcacaoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcacaoTipoDao extends JpaRepository<MarcacaoTipo, Long> {
}