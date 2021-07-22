package br.com.prolog.prologtest.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "marcacao_vinculo_inicio_fim")
public class MarcacaoVinculoInicioFim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long codigo;

    @OneToOne()
    @JoinColumn(name = "cod_marcacao_inicio", nullable = false,unique = true)
    private Marcacao marcacaoInicio;

    @OneToOne()
    @JoinColumn(name = "cod_marcacao_fim", nullable = false,unique = true)
    private Marcacao marcacaoFim;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public Marcacao getMarcacaoInicio() {
        return marcacaoInicio;
    }

    public void setMarcacaoInicio(Marcacao marcacaoInicio) {
        this.marcacaoInicio = marcacaoInicio;
    }

    public Marcacao getMarcacaoFim() {
        return marcacaoFim;
    }

    public void setMarcacaoFim(Marcacao marcacaoFim) {
        this.marcacaoFim = marcacaoFim;
    }

    @Override
    public String toString() {
        return "MarcacaoVinculoInicioFim{" +
                "codigo=" + codigo +
                ", marcacaoInicio=" + marcacaoInicio +
                ", marcacaoFim=" + marcacaoFim +
                '}';
    }
}