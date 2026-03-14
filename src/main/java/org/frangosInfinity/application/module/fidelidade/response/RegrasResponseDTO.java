package org.frangosInfinity.application.module.fidelidade.response;

import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;

public class RegrasResponseDTO
{
    private Long id;
    private Double pontosPorReal;
    private Integer diasExpiracao;
    private Integer pontosMinimosResgate;
    private Double valorDescontoPorBloco;
    private Integer pontosPorBloco;
    private Double valorMinimoProdutoDesconto;
    private Boolean ativo;
    private Boolean sucesso;
    private String mensagem;

    public RegrasResponseDTO() {}

    public static RegrasResponseDTO fromEntity(RegrasFidelidade regras)
    {
        if (regras == null)
        {
            return null;
        }

        RegrasResponseDTO dto = new RegrasResponseDTO();
        dto.setId(regras.getId());
        dto.setPontosPorReal(regras.getPontosPorReal());
        dto.setDiasExpiracao(regras.getDiasExpiracao());
        dto.setPontosMinimosResgate(regras.getPontosMinimosResgate());
        dto.setValorDescontoPorBloco(regras.getValorDescontoPorBloco());
        dto.setPontosPorBloco(regras.getPontosPorBloco());
        dto.setValorMinimoProdutoDesconto(regras.getValorMinimoProdutoDesconto());
        dto.setAtivo(regras.isAtivo());
        dto.setSucesso(true);
        return dto;
    }

    public static RegrasResponseDTO erro(String mensagem)
    {
        RegrasResponseDTO dto = new RegrasResponseDTO();
        dto.setSucesso(false);
        dto.setMensagem(mensagem);
        return dto;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Double getPontosPorReal()
    {
        return pontosPorReal;
    }

    public void setPontosPorReal(Double pontosPorReal)
    {
        this.pontosPorReal = pontosPorReal;
    }

    public Integer getDiasExpiracao()
    {
        return diasExpiracao;
    }
    public void setDiasExpiracao(Integer diasExpiracao)
    {
        this.diasExpiracao = diasExpiracao;
    }

    public Integer getPontosMinimosResgate()
    {
        return pontosMinimosResgate;
    }
    public void setPontosMinimosResgate(Integer pontosMinimosResgate)
    {
        this.pontosMinimosResgate = pontosMinimosResgate;
    }

    public Double getValorDescontoPorBloco()
    {
        return valorDescontoPorBloco;
    }

    public void setValorDescontoPorBloco(Double valorDescontoPorBloco)
    {
        this.valorDescontoPorBloco = valorDescontoPorBloco;
    }

    public Integer getPontosPorBloco()
    {
        return pontosPorBloco;
    }

    public void setPontosPorBloco(Integer pontosPorBloco)
    {
        this.pontosPorBloco = pontosPorBloco;
    }

    public Double getValorMinimoProdutoDesconto()
    {
        return valorMinimoProdutoDesconto;
    }

    public void setValorMinimoProdutoDesconto(Double valorMinimoProdutoDesconto)
    {
        this.valorMinimoProdutoDesconto = valorMinimoProdutoDesconto;
    }

    public Boolean getAtivo()
    {
        return ativo;
    }

    public void setAtivo(Boolean ativo)
    {
        this.ativo = ativo;
    }

    public Boolean getSucesso()
    {
        return sucesso;
    }
    public void setSucesso(Boolean sucesso)
    {
        this.sucesso = sucesso;
    }

    public String getMensagem()
    {
        return mensagem;
    }

    public void setMensagem(String mensagem)
    {
        this.mensagem = mensagem;
    }
}
