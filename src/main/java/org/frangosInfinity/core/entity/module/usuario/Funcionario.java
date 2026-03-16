package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.time.LocalDateTime;

public abstract class Funcionario extends Usuario
{
    protected String matricula;
    protected LocalDateTime dataContratacao;
    protected String turno;
    protected NivelAcesso nivelAcesso;
    protected double salario;

    public Funcionario() {}

    public Funcionario(String nome, String email, String senha,TipoUsuario usuario , String matricula, NivelAcesso nivelAcesso )
    {
        super(nome, email, senha , usuario);
        this.matricula = matricula;
        this.nivelAcesso = nivelAcesso;
        this.dataContratacao = LocalDateTime.now();
    }

    public String getMatricula()
    {
        return matricula;
    }

    public void setMatricula(String matricula)
    {
        this.matricula = matricula;
    }

    public LocalDateTime getDataContratacao()
    {
        return dataContratacao;
    }

    public void setDataContratacao(LocalDateTime dataContratacao)
    {
        this.dataContratacao = dataContratacao;
    }

    public String getTurno()
    {
        return turno;
    }

    public void setTurno(String turno)
    {
        this.turno = turno;
    }

    public NivelAcesso getNivelAcesso()
    {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso)
    {
        this.nivelAcesso = nivelAcesso;
    }

    public double getSalario()
    {
        return salario;
    }

    public void setSalario(double salario)
    {
        this.salario = salario;
    }
}
