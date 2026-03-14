package org.frangosInfinity.application.module.mesa.response;

import org.frangosInfinity.core.entity.module.mesa.Mesa;

public class MesaResponseDTO
{
    private Long id;
    private Integer numero;
    private Integer capacidade;
    private String localizacao;
    private Boolean disponivel;
    private Boolean ativa;
    private Long idIotConfig;
    private String mensagem;
    private Boolean sucesso;

    public MesaResponseDTO() {}

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

    public Boolean getDisponivel()
    {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel)
    {
        this.disponivel = disponivel;
    }

    public Boolean getAtiva()
    {
        return ativa;
    }

    public void setAtiva(Boolean ativa)
    {
        this.ativa = ativa;
    }

    public Long getIdIotConfig()
    {
        return idIotConfig;
    }

    public void setIdIotConfig(Long idIotConfig)
    {
        this.idIotConfig = idIotConfig;
    }

    public String getMensagem()
    {
        return mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }

    public Boolean getSucesso()
    {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso)
    {
        this.sucesso = sucesso;
    }

    public static MesaResponseDTO fromEntity(Mesa mesa)
    {
        MesaResponseDTO dto = new MesaResponseDTO();
        dto.setId(mesa.getId());
        dto.setNumero(mesa.getNumero());
        dto.setCapacidade(mesa.getCapacidade());
        dto.setLocalizacao(mesa.getLocalizacao());
        dto.setDisponivel(mesa.isDisponivel());
        dto.setAtiva(mesa.isAtiva());
        dto.setIdIotConfig(mesa.getIdIotConfig());
        dto.setSucesso(true);
        dto.setMensagem("Operação realizada com sucesso");
        return dto;
    }
}
