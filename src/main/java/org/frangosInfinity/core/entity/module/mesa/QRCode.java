package org.frangosInfinity.core.entity.module.mesa;

import java.time.LocalDateTime;
import java.util.UUID;

public class QRCode
{
    private Long id;
    private String codigo;
    private String urlAutenticacao;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataExpiracao;
    private Boolean ativo;
    private Boolean utilizado;
    private Long idMesa;
    private String tokenSessao;

    public QRCode()
    {
        this.codigo = UUID.randomUUID().toString().substring(0,8).toUpperCase();
        this.dataCriacao = LocalDateTime.now();
        this.dataExpiracao = this.dataCriacao.plusMinutes(2);
        this.ativo = true;
        this.utilizado = false;
        this.tokenSessao = UUID.randomUUID().toString();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getCodigo()
    {
        return codigo;
    }

    public void setCodigo(String codigo)
    {
        this.codigo = codigo;
    }

    public String getUrlAutenticacao()
    {
        return urlAutenticacao;
    }

    public void setUrlAutenticacao(String urlAutenticacao)
    {
        this.urlAutenticacao = urlAutenticacao;
    }

    public LocalDateTime getDataCriacao()
    {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao)
    {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataExpiracao()
    {
        return dataExpiracao;
    }

    public void setDataExpiracao(LocalDateTime dataExpiracao)
    {
        this.dataExpiracao = dataExpiracao;
    }

    public Boolean isAtivo()
    {
        return ativo;
    }

    public void setAtivo(Boolean ativo)
    {
        this.ativo = ativo;
    }

    public Boolean isUtilizado()
    {
        return utilizado;
    }

    public void setUtilizado(Boolean utilizado)
    {
        this.utilizado = utilizado;
    }

    public Long getIdMesa()
    {
        return idMesa;
    }

    public void setIdMesa(Long idMesa)
    {
        this.idMesa = idMesa;
    }

    public String getTokenSessao()
    {
        return tokenSessao;
    }

    public void setTokenSessao(String tokenSessao)
    {
        this.tokenSessao = tokenSessao;
    }

    public Boolean isExpirado()
    {
        return LocalDateTime.now().isAfter(dataExpiracao);
    }

    public Boolean podeSerUtilizado()
    {
        return ativo && !utilizado && !isExpirado();
    }

    public void utilizar()
    {
        if(!podeSerUtilizado())
        {
            throw new IllegalStateException("QR Code não pode ser utilizado");
        }

        this.utilizado = true;
        this.ativo = false;
    }

    public String toString()
    {
        return "QRCode{"+
                "codigo='" + codigo + '\'' +
                ", mesa=" + idMesa +
                ", expira=" + dataExpiracao +
                '}';
    }
}
