package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SubPedidoResponseDTO
{
    private Long id;
    private Long pedidoId;
    private String numeroPedido;
    private Long clienteID;
    private LocalDateTime dataHora;
    private StatusPedido status;
    private Double valorTotal;
    private Integer tempoPreparoMinutos;
    private String obsevacoes;
    private List<ItemPedidoResponseDTO> itens;

    public SubPedidoResponseDTO() {}

    public static SubPedidoResponseDTO fromEntity(SubPedido subPedido)
    {
        SubPedidoResponseDTO response = new SubPedidoResponseDTO();
        response.setId(subPedido.getId());
        response.setPedidoId(subPedido.getPedido().getId());
        response.setNumeroPedido(subPedido.getPedido().getNumeroPedido());
        response.setClienteID(subPedido.getClienteID());
        response.setDataHora(subPedido.getDate());
        response.setStatus(subPedido.getStatus());
        response.setValorTotal(subPedido.getValorTotal());
        response.setTempoPreparoMinutos(subPedido.getTempo_em_minutos());
        response.setObsevacoes(subPedido.getObsevacoes());

        if (subPedido.getItens() != null && !subPedido.getItens().isEmpty())
        {
            response.setItens(subPedido.getItens().stream()
                    .map(ItemPedidoResponseDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Long getClienteID() {
        return clienteID;
    }

    public void setClienteID(Long clienteID) {
        this.clienteID = clienteID;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getTempoPreparoMinutos() {
        return tempoPreparoMinutos;
    }

    public void setTempoPreparoMinutos(Integer tempoPreparoMinutos) {
        this.tempoPreparoMinutos = tempoPreparoMinutos;
    }

    public String getObsevacoes() {
        return obsevacoes;
    }

    public void setObsevacoes(String obsevacoes) {
        this.obsevacoes = obsevacoes;
    }

    public List<ItemPedidoResponseDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoResponseDTO> itens) {
        this.itens = itens;
    }
}
