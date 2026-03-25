package org.frangosInfinity.application.module.pagamento.response;

import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.frangosInfinity.core.enums.StatusPagamento;
import org.frangosInfinity.core.enums.TipoPagamento;

public class PIXResponseDTO
{
    private Long id;
    private Long pagamentoId;
    private String qrCode;
    private String codigoCopiaCola;
    private String dataEspiracao;
    private Boolean expirado;
    private Boolean sucesso;
    private String mensagem;

    public PIXResponseDTO() {}

    public static PIXResponseDTO fromEntity(TransacaoPIX pix)
    {
        PIXResponseDTO response = new PIXResponseDTO();
        response.setId(pix.getId());
        response.setId(pix.getPagamento().getId_Pagamento());
        response.setQrCode(pix.getQrCode());
        response.setCodigoCopiaCola(pix.getCodigoCopiaCola());
        response.setDataEspiracao(pix.getDataExpiracao().toString());
        response.setExpirado(pix.idExpirado());
        response.setSucesso(true);
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

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getCodigoCopiaCola() {
        return codigoCopiaCola;
    }

    public void setCodigoCopiaCola(String codigoCopiaCola) {
        this.codigoCopiaCola = codigoCopiaCola;
    }

    public String getDataEspiracao() {
        return dataEspiracao;
    }

    public void setDataEspiracao(String dataEspiracao) {
        this.dataEspiracao = dataEspiracao;
    }

    public Boolean getExpirado() {
        return expirado;
    }

    public void setExpirado(Boolean expirado) {
        this.expirado = expirado;
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
