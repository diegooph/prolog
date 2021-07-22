package br.com.prolog.prologtest.entity;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "marcacao")
public class Marcacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_tipo_marcacao", nullable = false)
    private MarcacaoTipo marcacaoTipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cpf_colaborador", nullable = false)
    private Colaborador colaborador;

    @Column(name = "data_hora_marcacao", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataMarcacao;

    @Column(name = "tipo_marcacao")
    @NotNull
    @Enumerated(EnumType.STRING)
    private TipoMarcacaoEnum tipoMarcacaoEnum;

    @OneToOne(fetch = FetchType.EAGER,targetEntity = MarcacaoVinculoInicioFim.class,mappedBy = "marcacaoInicio")
    @JoinColumn(name = "cod_marcacao_inicio", nullable = false,unique = true)
    private MarcacaoVinculoInicioFim marcacaoVinculoInicio;

    @OneToOne(fetch = FetchType.EAGER,targetEntity = MarcacaoVinculoInicioFim.class,mappedBy = "marcacaoFim")
    @JoinColumn(name = "cod_marcacao_Final", nullable = false,unique = true)
    private MarcacaoVinculoInicioFim marcacaoVinculoFinal;

    public long getCodigo() {
        return codigo;
    }

    public void setCodigo(long codigo) {
        this.codigo = codigo;
    }

    public MarcacaoVinculoInicioFim getMarcacaoVinculoInicio() {
        return marcacaoVinculoInicio;
    }

    public void setMarcacaoinicio(MarcacaoVinculoInicioFim marcacaoInicio) {
        this.marcacaoVinculoInicio = marcacaoInicio;
    }

    public MarcacaoVinculoInicioFim getMarcacaoVinculoFinal() {
        return marcacaoVinculoFinal;
    }

    public void setMarcacaoVinculoFinal(MarcacaoVinculoInicioFim marcacaoFinal) {
        this.marcacaoVinculoFinal = marcacaoFinal;
    }

    public MarcacaoTipo getMarcacaoTipo() {
        return marcacaoTipo;
    }

    public void setMarcacaoTipo(MarcacaoTipo marcacaoTipo) {
        this.marcacaoTipo = marcacaoTipo;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Date getDataMarcacao() {
        return dataMarcacao;
    }

    public void setDataMarcacao(Date dataMarcacao) {
        this.dataMarcacao = dataMarcacao;
    }

    public TipoMarcacaoEnum getTipoMarcacaoEnum() {
        return tipoMarcacaoEnum;
    }

    public void setTipoMarcacaoEnum(TipoMarcacaoEnum tipoMarcacaoEnum) {
        this.tipoMarcacaoEnum = tipoMarcacaoEnum;
    }

}
