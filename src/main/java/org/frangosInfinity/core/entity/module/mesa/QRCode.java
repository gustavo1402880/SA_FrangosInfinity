package org.frangosInfinity.core.entity.module.mesa;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "qr_code")
public class QRCode
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 8)
    private String codigo;

    @Column(name = "url_autenticacao", length = 255)
    private String urlAutenticacao;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_expiracao", nullable = false)
    private LocalDateTime dataExpiracao;

    @Column(nullable = false)
    private Boolean ativo;

    @Column(nullable = false)
    private Boolean utilizado;

    @ManyToOne
    @JoinColumn(name = "id_mesa", nullable = false)
    private Mesa mesa;

    @Column(name = "token_sessao", unique = true, length = 36)
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

    public Mesa getIdMesa()
    {
        return mesa;
    }

    public void setIdMesa(Mesa mesa)
    {
        this.mesa = mesa;
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
                ", mesa=" + mesa +
                ", expira=" + dataExpiracao +
                '}';
    }
}
