package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.enums.StatusPedido;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PedidoResponseDTO
{
    private Long id;
    private String numeroPedido;
    private LocalDateTime dataHora;
    private Long mesaId;
    private Long atendenteId;
    private Double valorTotal;
    private List<SubPedidoRequestDTO> subPedidos;
    private String tipo;
    private Boolean sucesso;
    private String mensagem;

    public PedidoResponseDTO() {}

    public static PedidoResponseDTO fromEntity(Pedido pedido)
    {
        PedidoResponseDTO response = new PedidoResponseDTO();
        response.setId(pedido.getId());
        response.setNumeroPedido(pedido.getNumeroPedido());
        response.setDataHora(pedido.getDataHora());
        response.setTipo(pedido.getTipo());
        response.setMesaId(pedido.getMesaId());
        response.setAtendenteId(pedido.getAtendenteId());
        response.setValorTotal(pedido.getValorTotal());

        if (pedido.getSubPedidos() != null && !pedido.getSubPedidos().isEmpty())
        {
            response.setSubPedidos(pedido.getSubPedidos().stream()
                    .map(SubPedidoResponseDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        response.setSucesso(true);
        return response;
    }

    public static PedidoResponseDTO erro(String mensagem)
    {
        PedidoResponseDTO responseDTO = new PedidoResponseDTO();
        responseDTO.setSucesso(false);
        responseDTO.setMensagem(mensagem);
        return responseDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Long getMesaId() {
        return mesaId;
    }

    public void setMesaId(Long mesaId) {
        this.mesaId = mesaId;
    }

    public Long getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId) {
        this.atendenteId = atendenteId;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<SubPedidoRequestDTO> getSubPedidos() {
        return subPedidos;
    }

    public void setSubPedidos(List<SubPedidoRequestDTO> subPedidos) {
        this.subPedidos = subPedidos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
