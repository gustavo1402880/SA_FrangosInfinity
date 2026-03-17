package org.frangosInfinity.config.security;

import org.frangosInfinity.core.entity.module.usuario.Funcionario;
import org.frangosInfinity.core.entity.module.usuario.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class UserDetailsImpl implements UserDetails
{
    private final Usuario usuario;
    private final Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Usuario usuario)
    {
        this.usuario = usuario;

        String role;
        if (usuario instanceof Funcionario)
        {
            role = "ROLE_"+((Funcionario) usuario).getNivelAcesso().name();
        }
        else
        {
            role = "ROLE_CLIENTE";
        }

        this.authorities = Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    public Usuario getUsuario()
    {
        return usuario;
    }

    public Long getId()
    {
        return usuario.getId();
    }

    public String getNome()
    {
        return usuario.getNome();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    @Override
    public String getPassword()
    {
        return usuario.getSenha();
    }

    @Override
    public String getUsername()
    {
        return usuario.getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return usuario.isAtivo();
    }
}
