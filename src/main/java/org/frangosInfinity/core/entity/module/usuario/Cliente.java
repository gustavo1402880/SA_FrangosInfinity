package org.frangosInfinity.core.entity.module.usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@PrimaryKeyJoinColumn(name = "id")
public class Cliente extends Usuario
{
    @Column(name = "id_sessao", length = 50)
    private String idSessao;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "total_gasto")
    private Double totalGasto;

    @Column(name = "pontos_fidelidade")
    private Integer pontosFidelidade;

    public Cliente() {}

    public Cliente(String nome, String email, String senha, TipoUsuario usuario ,String idSessao)
    {
        super(nome, email, senha , usuario);
        this.idSessao = idSessao;
        this.totalGasto = 0.0;
        this.pontosFidelidade = 0;
    }

    public String getIdSessao()
    {
        return idSessao;
    }

    public void setIdSessao(String idSessao)
    {
        this.idSessao = idSessao;
    }

    public LocalDate getDataNascimento()
    {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento)
    {
        this.dataNascimento = dataNascimento;
    }


    public Double getTotalGasto()
    {
        return totalGasto;
    }

    public void setTotalGasto(Double totalGasto)
    {
        this.totalGasto = totalGasto;
    }

    public Integer getPontosFidelidade()
    {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(Integer pontosFidelidade)
    {
        this.pontosFidelidade = pontosFidelidade;
    }

    public void acumularPontos(double valorGasto)
    {
        int pontos = (int) (valorGasto / 4);

        this.pontosFidelidade += pontos;

        this.totalGasto += valorGasto;
    }

    public boolean resgatarPontos(int pontos, double valorCompra)
    {
        if (this.pontosFidelidade >= pontos && pontos >= 20)
        {
            this.pontosFidelidade -= pontos;

            return true;
        }
        return false;
    }
}
