package org.frangosInfinity.application.module.relatorio.request;

import java.time.LocalDateTime;

public class RelatorioRequestDTO
{
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private LocalDateTime dataGeracao;
    private double totalVendas;
    private int totalPedidor;
    private double ticketMedio;

    public RelatorioRequestDTO(){}

    public RelatorioRequestDTO(LocalDateTime periodoInicio, LocalDateTime periodoFim, LocalDateTime dataGeracao, double totalVendas, int totalPedidor, double ticketMedio) {
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
        this.dataGeracao = dataGeracao;
        this.totalVendas = totalVendas;
        this.totalPedidor = totalPedidor;
        this.ticketMedio = ticketMedio;
    }

    public RelatorioRequestDTO(LocalDateTime periodoInicio, LocalDateTime periodoFim) {
        this.periodoInicio = periodoInicio;
        this.periodoFim = periodoFim;
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

    public double getTotalVendas() {
        return totalVendas;
    }

    public void setTotalVendas(double totalVendas) {
        this.totalVendas = totalVendas;
    }

    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

    public int getTotalPedidor() {
        return totalPedidor;
    }

    public void setTotalPedidor(int totalPedidor) {
        this.totalPedidor = totalPedidor;
    }

    /*private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private LocalDateTime dataGeracao;
    private double totalVendas;
    private int totalPedidos;
    private double ticketMedio;*/
}
