package org.frangosInfinity.core.entity.module.mesa;

import jakarta.persistence.*;

@Entity
@Table(name = "iot_config")
public class IotConfig
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_mesa")
    private Mesa mesa;

    @Column(name = "ip_dispositivo")
    private String ipDispositivo;

    @Column(nullable = false)
    private Integer porta;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private Boolean online;

    @Column(name = "versao_firmware", length = 20)
    private String versaoFirmware;

    public IotConfig() {}

    public IotConfig(Mesa mesa, String ipDispositivo, Integer porta)
    {
        this.mesa = mesa;
        this.ipDispositivo = ipDispositivo;
        this.porta = porta;
        this.modelo = "IoT Frango's Infinity";
        this.online = true;
        this.versaoFirmware = "1.0.0";
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Mesa getMesa()
    {
        return mesa;
    }

    public void setMesa(Mesa mesa)
    {
        this.mesa = mesa;
    }

    public String getIpDispositivo()
    {
        return ipDispositivo;
    }

    public void setIpDispositivo(String ipDispositivo)
    {
        this.ipDispositivo = ipDispositivo;
    }

    public Integer getPorta()
    {
        return porta;
    }

    public void setPorta(Integer porta)
    {
        this.porta = porta;
    }

    public String getModelo()
    {
        return modelo;
    }

    public void setModelo(String modelo)
    {
        this.modelo = modelo;
    }

    public Boolean isOnline()
    {
        return online;
    }

    public void setOnline(Boolean online)
    {
        this.online = online;
    }

    public String getVersaoFirmware()
    {
        return versaoFirmware;
    }

    public void setVersaoFirmware(String versaoFirmware)
    {
        this.versaoFirmware = versaoFirmware;
    }

    public String enviarComando(String comando)
    {
        if (!online)
        {
            return "Erro dispositivo não encontrado";
        }

        switch (comando)
        {
            case "GERAR_QR":
                return "QR_CODE_GERADO";
            case "STATUS":
                return "ONLINE|" + versaoFirmware;
            default:
                return "COMANDO_NAO_RECONHECIDO";
        }
    }

    public String toString()
    {
        return "IoTConfig [Mesa: " + mesa + ", IP: " + ipDispositivo + ":" + porta +
                ", Online: " + online + "]";
    }
}
