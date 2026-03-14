package org.frangosInfinity.application.module.mesa.request;

public class MesaRequestDTO
{
    private Integer numero;
    private Integer capacidade;
    private String localizacao;
    private String acao;

    public MesaRequestDTO() {}

    public MesaRequestDTO(Integer numero, Integer capacidade, String localizacao)
    {
        this.numero = numero;
        this.capacidade = capacidade;
        this.localizacao = localizacao;
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

    public void setCapacidade(Integer capacidade)
    {
        this.capacidade = capacidade;
    }

    public String getLocalizacao()
    {
        return localizacao;
    }

    public void setLocalizacao(String localizacao)
    {
        this.localizacao = localizacao;
    }

    public String getAcao()
    {
        return acao;
    }

    public void setAcao(String acao)
    {
        this.acao = acao;
    }
}
