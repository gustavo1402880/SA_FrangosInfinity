package org.frangosInfinity.core.entity.module.produto;

public class Produto {

    // Atributos

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Double preco;
    private Double custo;
    private Integer tempoPreparoMinuto;
    private boolean disponivel;
    private String imagemUrl;
    private Integer vendasUltimos30dias;
    private Double precoPendenteAprovacao;
    private Categoria categoria;

    // Construtores

    public Produto() {};

    public Produto(String codigo, String nome, String descricao, Double preco, Double custo, Integer tempoPreparoMinuto, boolean disponivel, String imagemUrl, Integer vendasUltimos300dias, Double precoPendenteAprovacao,Categoria categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.custo = custo;
        this.tempoPreparoMinuto = tempoPreparoMinuto;
        this.disponivel = disponivel;
        this.imagemUrl = imagemUrl;
        this.vendasUltimos30dias = vendasUltimos300dias;
        this.precoPendenteAprovacao = precoPendenteAprovacao;
        this.categoria = categoria;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public boolean getDisponivel(){
        return disponivel;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public void setCusto(Double custo) {
        this.custo = custo;
    }

    public void setTempoPreparoMinuto(Integer tempoPreparoMinuto) {
        this.tempoPreparoMinuto = tempoPreparoMinuto;
    }

    public void setVendasUltimos300dias(Integer vendasUltimos300dias) {
        this.vendasUltimos30dias = vendasUltimos300dias;
    }

    public void setPrecoPendenteAprovacao(Double precoPendenteAprovacao) {
        this.precoPendenteAprovacao = precoPendenteAprovacao;
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

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public int getTempoPreparoMinuto() {
        return tempoPreparoMinuto;
    }

    public void setTempoPreparoMinuto(int tempoPreparoMinuto) {
        this.tempoPreparoMinuto = tempoPreparoMinuto;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public Integer getVendasUltimos300dias() {
        return vendasUltimos30dias;
    }

    public void setVendasUltimos300dias(int vendasUltimos300dias) {
        this.vendasUltimos30dias = vendasUltimos300dias;
    }

    public Double getPrecoPendenteAprovacao() {
        return precoPendenteAprovacao;
    }

    public void setPrecoPendenteAprovacao(double precoPendenteAprovacao) {
        this.precoPendenteAprovacao = precoPendenteAprovacao;
    }
}
