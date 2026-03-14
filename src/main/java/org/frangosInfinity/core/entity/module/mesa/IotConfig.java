package org.frangosInfinity.core.entity.module.mesa;

public class IotConfig
{
    private Long id;
    private Long idMesa;
    private String ipDispositivo;
    private Integer porta;
    private String modelo;
    private Boolean online;
    private String versaoFirmware;

    public IotConfig(Long idMesa, String ipDispositivo, Integer porta)
    {
        this.idMesa = idMesa;
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

    public Long getIdMesa()
    {
        return idMesa;
    }

    public void setIdMesa(Long idMesa)
    {
        this.idMesa = idMesa;
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

        System.out.println("[IoT - Mesa " + idMesa + "] Comando enviado: " + comando);

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
        return "IoTConfig [Mesa: " + idMesa + ", IP: " + ipDispositivo + ":" + porta +
                ", Online: " + online + "]";
    }
}
