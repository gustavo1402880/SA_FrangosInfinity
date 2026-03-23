package org.frangosInfinity.core.entity.module.relatorio;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "relatorio_vendas")
public class RelatorioVendas
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "periodo_inicio", nullable = false)
    private LocalDateTime periodoInicio;

    @Column(name = "periodo_fim", nullable = false)
    private LocalDateTime periodoFim;

    @Column(name = "data_geracao", nullable = false, updatable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "total_vendas", nullable = false)
    private Double totalVendas;

    @Column(name = "total_pedidos", nullable = false)
    private Integer totalPedidos;

    @Column(name = "ticket_medio", nullable = false)
    private Double ticketMedio;

    @Column(name = "total_produtos_vendidos")
    private Integer totalProdutosVendidos;

    public RelatorioVendas(){}

    public RelatorioVendas(LocalDateTime periodoInicio, LocalDateTime periodoFim, Double totalVendas, Integer totalPedidos, Double ticketMedio)
    {
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.totalVendas = totalVendas;
        this.totalPedidos = totalPedidos;
        this.ticketMedio = ticketMedio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDateTime periodoFim) {
        this.periodoFim = periodoFim;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public Double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(Double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public Integer getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Integer totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public Double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(Double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

    public Integer getTotalProdutosVendidos() {
        return totalProdutosVendidos;
    }

    public void setTotalProdutosVendidos(Integer totalProdutosVendidos) {
        this.totalProdutosVendidos = totalProdutosVendidos;
    }

    public void calcularTicketMedio()
    {
        if (totalPedidos > 0)
        {
            this.ticketMedio = totalVendas / totalPedidos;
        }
        else
        {
            this.ticketMedio = 0.0;
        }
    }
}
