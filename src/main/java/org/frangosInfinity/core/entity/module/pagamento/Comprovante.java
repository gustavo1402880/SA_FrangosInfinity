package org.frangosInfinity.core.entity.module.pagamento;

import org.frangosInfinity.core.enums.TipoPagamento;

import java.time.LocalDateTime;
import java.util.List;

public class Comprovante
{
    private Long id;
    private Long idPagamento;
    private String numero;
    private LocalDateTime dataHora;
    private String cnpj;
    private List<Long> itensId;
    private Double valorTotal;
    private TipoPagamento formaPagamento;
    private String qrCodeString;

    public Comprovante() {}

    public Comprovante(Long idPagamento, String numero, LocalDateTime dataHora, String cnpj, List<Long> itensId, Double valorTotal, TipoPagamento formaPagamento, String qrCodeString)
    {
        this.idPagamento = idPagamento;
        this.numero = numero;
        this.dataHora = dataHora;
        this.cnpj = cnpj;
        this.itensId = itensId;
        this.valorTotal = valorTotal;
        this.formaPagamento = formaPagamento;
        this.qrCodeString = qrCodeString;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPagamento() {
        return idPagamento;
    }

    public void setIdPagamento(Long idPagamento) {
        this.idPagamento = idPagamento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public List<Long> getItensId() {
        return itensId;
    }

    public void setItensId(List<Long> itensId) {
        this.itensId = itensId;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public TipoPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(TipoPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getQrCodeString() {
        return qrCodeString;
    }

    public void setQrCodeString(String qrCodeString) {
        this.qrCodeString = qrCodeString;
    }
}
