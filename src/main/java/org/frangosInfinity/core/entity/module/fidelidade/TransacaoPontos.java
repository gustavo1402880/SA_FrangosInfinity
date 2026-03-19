package org.frangosInfinity.core.entity.module.fidelidade;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.TipoTransacaoPontos;

import java.time.LocalDateTime;

@Entity
@Table(name = "transacao_pontos")
public class TransacaoPontos
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pontos_fidelidade_id", nullable = false)
    private PontosFidelidade pontosFidelidade;

    @Column(nullable = false)
    private LocalDateTime data;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_transacao", nullable = false)
    private TipoTransacaoPontos tipoTransacaoPontos;

    @Column(nullable = false)
    private Integer quantidade;

    public TransacaoPontos () {}

    public TransacaoPontos(PontosFidelidade pontosFidelidade, TipoTransacaoPontos tipoTransacaoPontos, Integer quantidade)
    {
        this.pontosFidelidade = pontosFidelidade;
        this.tipoTransacaoPontos = tipoTransacaoPontos;
        this.data = LocalDateTime.now();
        this.quantidade = quantidade;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public PontosFidelidade getPontosFidelidade()
    {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(PontosFidelidade pontosFidelidade)
    {
        this.pontosFidelidade = pontosFidelidade;
    }

    public LocalDateTime getData()
    {
        return data;
    }

    public void setData(LocalDateTime data)
    {
        this.data = data;
    }

    public TipoTransacaoPontos getTipoTransacaoPontos()
    {
        return tipoTransacaoPontos;
    }

    public void setTipoTransacaoPontos(TipoTransacaoPontos tipoTransacaoPontos)
    {
        this.tipoTransacaoPontos = tipoTransacaoPontos;
    }

    public Integer getQuantidade()
    {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade)
    {
        this.quantidade = quantidade;
    }

    public boolean isAcumulo()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.ACUMULO;
    }

    public boolean isResgate()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.RESGATE;
    }

    public boolean isExpiracao()
    {
        return this.tipoTransacaoPontos == TipoTransacaoPontos.EXPIRACAO;
    }
}
