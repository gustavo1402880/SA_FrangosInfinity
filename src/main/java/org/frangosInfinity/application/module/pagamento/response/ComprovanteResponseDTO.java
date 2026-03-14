package org.frangosInfinity.application.module.pagamento.response;

import org.frangosInfinity.core.entity.module.pagamento.Comprovante;

public class ComprovanteResponseDTO
{
    private Long id;
    private Long pagamentoId;
    private String numero;
    private String dataHora;
    private Double valorTotal;
    private String texto;
    private Boolean sucesso;
    private String mensagem;

    public ComprovanteResponseDTO() {}

    public static ComprovanteResponseDTO fromEntity(Comprovante comprovante)
    {
        ComprovanteResponseDTO response = new ComprovanteResponseDTO();
        response.setId(comprovante.getId());
        response.setPagamentoId(comprovante.getIdPagamento());
        response.setNumero(comprovante.getNumero());
        response.setDataHora(comprovante.getDataHora().toString());
        response.setValorTotal(comprovante.getValorTotal());
        response.setSucesso(true);
        return response;
    }

    public static ComprovanteResponseDTO erro(String mensagem)
    {
        ComprovanteResponseDTO response = new ComprovanteResponseDTO();
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

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
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
