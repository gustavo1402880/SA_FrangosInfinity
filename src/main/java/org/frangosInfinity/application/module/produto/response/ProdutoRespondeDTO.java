package org.frangosInfinity.application.module.produto.response;

import org.frangosInfinity.core.entity.module.produto.Categoria;
import org.frangosInfinity.core.entity.module.produto.Produto;

public class ProdutoRespondeDTO
{
    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Double preco;
    private Integer tempoPreparoMinuto;
    private Boolean disponivel;
    private String imagemUrl;
    private Integer vendasUltimos30dias;
    private Boolean precoPendente;
    private String nomeCategoria;
    private Long categoriaId;
    private Integer estoqueAtual;
    private Boolean estoqueBaixo;
    private Boolean estoqueAlto;
    private Boolean sucesso;
    private String mensagem;

    public ProdutoRespondeDTO() {}

    public static ProdutoRespondeDTO fromEntity(Produto produto)
    {
        ProdutoRespondeDTO response = new ProdutoRespondeDTO();
        response.setId(produto.getId());
        response.setCodigo(produto.getCodigo());
        response.setNome(produto.getNome());
        response.setDescricao(produto.getDescricao());
        response.setPreco(produto.getPreco());
        response.setTempoPreparoMinuto(produto.getTempoPreparoMinuto());
        response.setDisponivel(produto.getDisponivel());
        response.setImagemUrl(produto.getImagemUrl());
        response.setVendasUltimos30dias(produto.getVendasUltimos30dias());
        response.setPrecoPendente();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Integer getTempoPreparoMinuto() {
        return tempoPreparoMinuto;
    }

    public void setTempoPreparoMinuto(Integer tempoPreparoMinuto) {
        this.tempoPreparoMinuto = tempoPreparoMinuto;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Integer getVendasUltimos30dias() {
        return vendasUltimos30dias;
    }

    public void setVendasUltimos30dias(Integer vendasUltimos30dias) {
        this.vendasUltimos30dias = vendasUltimos30dias;
    }

    public Boolean getPrecoPendente() {
        return precoPendente;
    }

    public void setPrecoPendente(Boolean precoPendente) {
        this.precoPendente = precoPendente;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Integer getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(Integer estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public Boolean getEstoqueBaixo() {
        return estoqueBaixo;
    }

    public void setEstoqueBaixo(Boolean estoqueBaixo) {
        this.estoqueBaixo = estoqueBaixo;
    }

    public Boolean getEstoqueAlto() {
        return estoqueAlto;
    }

    public void setEstoqueAlto(Boolean estoqueAlto) {
        this.estoqueAlto = estoqueAlto;
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
