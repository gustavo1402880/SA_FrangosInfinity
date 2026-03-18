package org.frangosInfinity.core.entity.module.mesa;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "mesa")
public class Mesa
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false)
    private Integer capacidade;

    @Column(length = 100)
    private String localizacao;

    @Column(nullable = false)
    private Boolean disponivel;

    @Column(nullable = false)
    private Boolean ativa;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_iot_config")
    private IotConfig iotConfig;

    @OneToMany(mappedBy = "mesa", cascade = CascadeType.ALL)
    private List<QRCode> historicoQRCodes;

    public Mesa() {}

    public Mesa(int numero, int capacidade, String localizacao)
    {
        this.numero = numero;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
        this.disponivel = true;
        this.ativa = true;
        this.historicoQRCodes = new ArrayList<>();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getNumero()
    {
        return numero;
    }

    public void setNumero(Integer numero)
    {
        this.numero = numero;
    }

    public Integer getCapacidade()
    {
        return capacidade;
    }

    public void setCapacidade(int capacidade)
    {
        this.capacidade = capacidade;
    }

    public String getLocalizacao()
    {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }

    public Boolean isDisponivel()
    {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel)
    {
        this.disponivel = disponivel;
    }

    public Boolean isAtiva()
    {
        return ativa;
    }

    public void setAtiva(Boolean ativa)
    {
        this.ativa = ativa;
    }

    public IotConfig getIotConfig()
    {
        return iotConfig;
    }

    public void setIotConfig(IotConfig iotConfig)
    {
        this.iotConfig = iotConfig;
    }

    public List<QRCode> getHistoricoQRCodes()
    {
        return historicoQRCodes;
    }

    public void adicionarQRCodeHistorico(QRCode idQRCode)
    {
        this.historicoQRCodes.add(idQRCode);
    }

    public void ocuparMesa()
    {
        this.disponivel = false;
    }

    public void liberarMesa()
    {
        this.disponivel = true;
    }

    @Override
    public String toString()
    {
        return "Mesa " + numero + " (Capacidade: " + capacidade + ") - " +
                (disponivel ? "Disponível" : "Ocupada");
    }
}
