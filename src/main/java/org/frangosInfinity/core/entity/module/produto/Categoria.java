package org.frangosInfinity.core.entity.module.produto;

public class Categoria {

    // Atributos

    private Long id;
    private String nome;
    private String descricao;
    private int ordemExibicao;

    // Construtores


    public Categoria() {}

    public Categoria(Long id, String nome, String descricao, int ordemExibicao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ordemExibicao = ordemExibicao;
    }

    // Getters & Setters

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getOrdemExibicao() {
        return ordemExibicao;
    }

    public void setOrdemExibicao(int ordemExibicao) {
        this.ordemExibicao = ordemExibicao;
    }
}
