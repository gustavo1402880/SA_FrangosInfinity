package org.frangosInfinity.application.module.pagamento.request;

public class PIXRequestDTO
{
    private Long pagamentoId;
    private Integer tempoExpiracaoSegundos;

    public PIXRequestDTO() {}

    public PIXRequestDTO(Long pagamentoId, Integer tempoExpiracaoSegundos)
    {
        this.pagamentoId = pagamentoId;
        this.tempoExpiracaoSegundos = tempoExpiracaoSegundos;
    }

    public Long getPagamentoId() {
        return pagamentoId;
    }

    public void setPagamentoId(Long pagamentoId) {
        this.pagamentoId = pagamentoId;
    }

    public Integer getTempoExpiracaoSegundos() {
        return tempoExpiracaoSegundos;
    }

    public void setTempoExpiracaoSegundos(Integer tempoExpiracaoSegundos) {
        this.tempoExpiracaoSegundos = tempoExpiracaoSegundos;
    }

    public Boolean valido()
    {
        return pagamentoId != null && pagamentoId > 0;
    }
}
