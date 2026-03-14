package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.TipoUsuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario
{
    private String idSessao;
    private LocalDate dataNascimento;
    private double totalGasto;
    private int pontosFidelidade;

    public Cliente() {}

    public Cliente(String nome, String email, String senha, TipoUsuario usuario ,String idSessao)
    {
        super(nome, email, senha , usuario);
        this.idSessao = idSessao;
        this.totalGasto = 0;
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


    public double getTotalGasto()
    {
        return totalGasto;
    }

    public void setTotalGasto(double totalGasto)
    {
        this.totalGasto = totalGasto;
    }

    public int getPontosFidelidade()
    {
        return pontosFidelidade;
    }

    public void setPontosFidelidade(int pontosFidelidade)
    {
        this.pontosFidelidade = pontosFidelidade;
    }

    public String getTipoUsuario()
    {
        return "CLIENTE";
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
