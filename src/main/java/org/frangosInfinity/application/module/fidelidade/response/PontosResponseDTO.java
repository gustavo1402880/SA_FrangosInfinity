package org.frangosInfinity.application.module.fidelidade.response;

import java.util.List;

public class PontosResponseDTO
{
    private Long id;
    private Long clienteId;
    private Integer saldo;
    private String dataValidade;
    private List<TransacaoResponseDTO> historico;
    private String mensagem;
    private Boolean sucesso;

    public PontosResponseDTO() {}

    public PontosResponseDTO(Long id, Long clienteId, Integer saldo, String mensagem)
    {
        this.id = id;
        this.clienteId = clienteId;
        this.saldo = saldo;
        this.mensagem = mensagem;
    }

    public static PontosResponseDTO erro(String mensagem)
    {
        PontosResponseDTO dto = new PontosResponseDTO(null, null, 0, mensagem);
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

    public Long getClienteId()
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId)
    {
        this.clienteId = clienteId;
    }

    public Integer getSaldo()
    {
        return saldo;
    }

    public void setSaldo(Integer saldo)
    {
        this.saldo = saldo;
    }

    public String getDataValidade()
    {
        return dataValidade;
    }

    public void setDataValidade(String dataValidade)
    {
        this.dataValidade = dataValidade;
    }

    public List<TransacaoResponseDTO> getHistorico()
    {
        return historico;
    }

    public void setHistorico(List<TransacaoResponseDTO> historico)
    {
        this.historico = historico;
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
}
