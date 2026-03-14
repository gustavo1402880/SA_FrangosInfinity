package org.frangosInfinity.core.entity.module.relatorio;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class RelatorioVendas {
    private Long id;
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private LocalDateTime dataGeracao;
    private double totalVendas;
    private int totalPedidos;
    private double ticketMedio;

    RelatorioVendas(){}

    public RelatorioVendas(Long id, LocalDateTime periodoInicio, LocalDateTime dataGeracao, LocalDateTime periodoFim, int totalPedidos, double totalVendas, double ticketMedio) {
        this.id = id;
        this.periodoInicio = periodoInicio;
        this.dataGeracao = dataGeracao;
        this.periodoFim = periodoFim;
        this.totalPedidos = totalPedidos;
        this.totalVendas = totalVendas;
        this.ticketMedio = ticketMedio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPeriodoFim() {
        return periodoFim;
    }

    public void setPeriodoFim(LocalDateTime periodoFim) {
        this.periodoFim = periodoFim;
    }

    public LocalDateTime getPeriodoInicio() {
        return periodoInicio;
    }

    public void setPeriodoInicio(LocalDateTime periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public void setDataGeracao(LocalDateTime dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public int getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(int totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }
}
