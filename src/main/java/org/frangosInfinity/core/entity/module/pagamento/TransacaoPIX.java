package org.frangosInfinity.core.entity.module.pagamento;

import java.time.LocalDateTime;

public class TransacaoPIX
{
    private Long id;
    private Long pagamentoId;
    private String qrCode;
    private String codigoCopiaCola;
    private Integer tempoExpiracaoSegundos;
    private LocalDateTime dataExpiracao;

    public TransacaoPIX() {}

    public TransacaoPIX(Long pagamentoId, String qrCode, String codigoCopiaCola)
    {
        this.pagamentoId = pagamentoId;
        this.qrCode = qrCode;
        this.codigoCopiaCola = codigoCopiaCola;
        this.tempoExpiracaoSegundos = 600;
        this.dataExpiracao = LocalDateTime.now().plusSeconds(tempoExpiracaoSegundos);
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

    public Integer getTempoExpiracaoSegundos() {
        return tempoExpiracaoSegundos;
    }

    public void setTempoExpiracaoSegundos(Integer tempoExpiracaoSegundos) {
        this.tempoExpiracaoSegundos = tempoExpiracaoSegundos;
    }

    public LocalDateTime getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public Boolean idExpirado()
    {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }

    public void renovar()
    {
        this.dataExpiracao = LocalDateTime.now().plusSeconds(tempoExpiracaoSegundos);
    }

    @Override
    public String toString()
    {
        return "TransacaoPix{" +
                "id=" + id +
                ", expira em=" + dataExpiracao +
                ", codigo='" + codigoCopiaCola + '\'' +
                '}';
    }

}
