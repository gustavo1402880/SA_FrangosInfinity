package org.frangosInfinity.application.module.relatorio.response;

import org.frangosInfinity.application.module.mesa.response.MesaResponseDTO;
import org.frangosInfinity.application.module.relatorio.request.RelatorioRequestDTO;
import org.frangosInfinity.core.entity.module.relatorio.RelatorioVendas;

import java.time.LocalDateTime;

public class RelatorioResponseDTO
{
    private LocalDateTime periodoInicio;
    private LocalDateTime periodoFim;
    private LocalDateTime dataGeracao;
    private double totalVendas;
    private int totalPedidor;
    private double ticketMedio;
    private String mensagem;
    private boolean sucesso;

    public RelatorioResponseDTO() {}

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

    public int getTotalPedidor() {
        return totalPedidor;
    }

    public void setTotalPedidor(int totalPedidor) {
        this.totalPedidor = totalPedidor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public double getTicketMedio() {
        return ticketMedio;
    }

    public void setTicketMedio(double ticketMedio) {
        this.ticketMedio = ticketMedio;
    }

    public boolean isSucesso() {
        return sucesso;
    }

    public void setSucesso(boolean sucesso) {
        this.sucesso = sucesso;
    }

    public static RelatorioResponseDTO fromEntity(RelatorioVendas relatorioVendas)
    {
        RelatorioResponseDTO dto = new RelatorioResponseDTO();

        dto.setPeriodoInicio(relatorioVendas.getPeriodoInicio());
        dto.setPeriodoFim(relatorioVendas.getPeriodoFim());
        dto.setDataGeracao(relatorioVendas.getDataGeracao());
        dto.setTotalVendas(relatorioVendas.getTotalVendas());
        dto.setTotalPedidor(relatorioVendas.getTotalPedidos());
        dto.setTicketMedio(relatorioVendas.getTicketMedio());
        dto.setSucesso(true);
        dto.setMensagem("Operação realizada com sucesso");

        return dto;
    }
}
