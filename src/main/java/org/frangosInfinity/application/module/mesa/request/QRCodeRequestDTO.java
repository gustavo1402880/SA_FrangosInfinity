package org.frangosInfinity.application.module.mesa.request;

public class QRCodeRequestDTO
{
    private Long idMesa;
    private String tokenAcesso;
    private Integer tempoExpiracaoSgundos;

    public QRCodeRequestDTO() {}

    public QRCodeRequestDTO(Long idMesa, String tokenAcesso)
    {
        this.idMesa = idMesa;
        this.tokenAcesso = tokenAcesso;
    }

    public Long getIdMesa()
    {
        return idMesa;
    }

    public void setIdMesa(Long idMesa)
    {
        this.idMesa = idMesa;
    }

    public String getTokenAcesso()
    {
        return tokenAcesso;
    }

    public void setTokenAcesso(String tokenAcesso)
    {
        this.tokenAcesso = tokenAcesso;
    }

    public Integer getTempoExpiracaoSgundos()
    {
        return tempoExpiracaoSgundos;
    }

    public void setTempoExpiracaoSgundos(Integer tempoExpiracaoSgundos)
    {
        this.tempoExpiracaoSgundos = tempoExpiracaoSgundos;
    }

    public Boolean isValid()
    {
        return idMesa != null && tokenAcesso != null && !tokenAcesso.isEmpty();
    }
}
