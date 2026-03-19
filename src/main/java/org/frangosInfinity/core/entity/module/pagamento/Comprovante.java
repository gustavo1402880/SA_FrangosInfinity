package org.frangosInfinity.core.entity.module.pagamento;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.TipoPagamento;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprovante")
public class Comprovante
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pagamento_id", nullable = false, unique = true)
    private Pagamento pagamento;

    @Column(unique = true, nullable = false, length = 20)
    private String numero;

    @Column(name = "data_hora", nullable = false, updatable = false)
    private LocalDateTime dataHora;

    @Column(length = 18)
    private String cnpj;

    @Column(name = "lista_itens", nullable = false)
    private List<Long> itensId;

    @Column(name = "valor-total", nullable = false)
    private Double valorTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "forma_pagamento", nullable = false)
    private TipoPagamento formaPagamento;

    @Column(name = "qr_code", length = 500)
    private String qrCodeString;

    public Comprovante() {}

    public Comprovante(Pagamento pagamento, String numero, LocalDateTime dataHora, String cnpj, List<Long> itensId, Double valorTotal, TipoPagamento formaPagamento, String qrCodeString)
    {
        this.pagamento = pagamento;
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

    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento)
    {
        this.pagamento = pagamento;
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
