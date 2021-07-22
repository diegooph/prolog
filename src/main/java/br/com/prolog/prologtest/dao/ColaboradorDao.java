package br.com.prolog.prologtest.dao;

import br.com.prolog.prologtest.entity.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColaboradorDao extends JpaRepository<Colaborador, String> {
     List<Colaborador> findColaboradorByNomeContains(String nome);
    Colaborador getColaboradorByNome(String nome);
}