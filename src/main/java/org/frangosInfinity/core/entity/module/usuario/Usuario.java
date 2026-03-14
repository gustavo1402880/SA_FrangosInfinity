package org.frangosInfinity.core.entity.module.usuario;

import org.frangosInfinity.core.enums.TipoUsuario;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class Usuario
{
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String senha;
    private LocalDateTime dataCadastro;
    private boolean ativo;
    private TipoUsuario usuario;


    public Usuario() {}

    public Usuario(String nome, String email, String senha, TipoUsuario usuario)
    {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.dataCadastro = LocalDateTime.now();
        this.ativo = true;
        this.usuario = usuario;
    }

    public TipoUsuario getUsuario() {
        return usuario;
    }

    public void setUsuario(TipoUsuario usuario) {
        this.usuario = usuario;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getSenha()
    {
        return senha;
    }

    public void setSenha(String senha)
    {
        this.senha = senha;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone)
    {
        this.telefone = telefone;
    }

    public LocalDateTime getDataCadastro()
    {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro)
    {
        this.dataCadastro = dataCadastro;
    }

    public boolean isAtivo()
    {
        return ativo;
    }

    public void setAtivo(boolean ativo)
    {
        this.ativo = ativo;
    }

    public boolean validarEmail()
    {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public void ativar()
    {
        this.ativo = true;
    }

    public void desativar()
    {
        this.ativo = false;
    }

    @Override
    public String toString()
    {
        return "Usuario{" + "id=" + id + ", nome='" + nome + '\'' + ", email='" + email + '\'' + ", ativo=" + ativo + '}';
    }

}
