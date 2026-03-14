package org.frangosInfinity.application.module.pagamento.response;

import org.frangosInfinity.application.module.mesa.response.QRCodeResponseDTO;
import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;

public class PagamentoResponseDTO
{
    private Long id;
    private Long subPedidoId;
    private String dataHora;
    private Double valor;
    private StatusPagamento status;
    private TipoPagamento tipo;
    private String codigoTransacao;
    private Boolean sucesso;
    private String mensagem;

    public PagamentoResponseDTO() {}

    public static PagamentoResponseDTO fromEntity(Pagamento pagamento)
    {
        PagamentoResponseDTO response = new PagamentoResponseDTO();
        response.setId(pagamento.getId_Pagamento());
        response.setSubPedidoId(pagamento.getId_SubPedido());
        response.setDataHora(pagamento.getDataHora().toString());
        response.setValor(pagamento.getValor());
        response.setStatus(pagamento.getStatusPagamento());
        response.setTipo(pagamento.getTipoPagamento());
        response.setCodigoTransacao(pagamento.getCodigoTransacao());
        response.setSucesso(true);
        return response;
    }

    public static PagamentoResponseDTO erro(String mensagem)
    {
        PagamentoResponseDTO response = new PagamentoResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSubPedidoId() {
        return subPedidoId;
    }

    public void setSubPedidoId(Long subPedidoId) {
        this.subPedidoId = subPedidoId;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public void setStatus(StatusPagamento status) {
        this.status = status;
    }

    public TipoPagamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoPagamento tipo) {
        this.tipo = tipo;
    }

    public String getCodigoTransacao() {
        return codigoTransacao;
    }

    public void setCodigoTransacao(String codigoTransacao) {
        this.codigoTransacao = codigoTransacao;
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
