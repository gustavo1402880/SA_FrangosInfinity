package org.frangosInfinity.application.module.usuario.response;

import org.frangosInfinity.core.entity.module.usuario.Funcionario;
import org.frangosInfinity.core.entity.module.usuario.Usuario;
import org.frangosInfinity.core.enums.NivelAcesso;
import org.frangosInfinity.core.enums.TipoUsuario;

public class UsuarioResponseDTO
{
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String dataCadastro;
    private Boolean ativo;
    private TipoUsuario tipoUsuario;
    private NivelAcesso nivelAcesso;
    private Boolean sucesso;
    private String mensagem;

    public UsuarioResponseDTO() {}

    public static UsuarioResponseDTO fromEntity(Usuario usuario)
    {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setId(usuario.getId());
        response.setNome(usuario.getNome());
        response.setEmail(usuario.getEmail());
        response.setTelefone(usuario.getTelefone());
        response.setDataCadastro(usuario.getDataCadastro().toString());
        response.setAtivo(usuario.isAtivo());
        response.setTipoUsuario(usuario.getUsuario());

        if (usuario instanceof Funcionario)
        {
            response.setNivelAcesso(((Funcionario) usuario).getNivelAcesso());
        }

        response.setSucesso(true);
        return response;
    }

    public static UsuarioResponseDTO erro(String mensagem)
    {
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public NivelAcesso getNivelAcesso() {
        return nivelAcesso;
    }

    public void setNivelAcesso(NivelAcesso nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
