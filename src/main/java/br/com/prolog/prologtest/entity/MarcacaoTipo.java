package br.com.prolog.prologtest.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "marcacao_tipo")
public class MarcacaoTipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long codigo;

    @NotBlank(message = "Este campo é Obrigatório")
    @NotNull
    @Column(columnDefinition="text")
    private String nome;

    @NotBlank(message = "Este campo é Obrigatório")
    @NotNull
    @Column(name ="tempo_recomendado_minutos")
    private long tempoRecomendadoMinutos;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getTempoRecomendadoMinutos() {
        return tempoRecomendadoMinutos;
    }

    public void setTempoRecomendadoMinutos(long tempoRecomendadoMinutos) {
        this.tempoRecomendadoMinutos = tempoRecomendadoMinutos;
    }

    @Override
    public String toString() {
        return "MarcacaoTipo{" +
                "codigo=" + codigo +
                ", nome='" + nome + '\'' +
                ", tempoRecomendadoMinutos=" + tempoRecomendadoMinutos +
                '}';
    }
}