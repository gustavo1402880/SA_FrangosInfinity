package org.frangosInfinity.core.entity.module.usuario;

import jakarta.persistence.*;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

import java.time.LocalDateTime;

@Entity
@Table(name = "funcionario")
@PrimaryKeyJoinColumn(name = "id")
public abstract class Funcionario extends Usuario
{
    @Column(unique = true, length = 20)
    protected String matricula;

    @Column(name = "data_contratacao")
    protected LocalDateTime dataContratacao;

    @Column(length = 20)
    protected String turno;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_acesso", length = 20)
    protected NivelAcesso nivelAcesso;

    @Column()
    protected Double salario;

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

    public Double getSalario()
    {
        return salario;
    }

    public void setSalario(Double salario)
    {
        this.salario = salario;
    }
}
