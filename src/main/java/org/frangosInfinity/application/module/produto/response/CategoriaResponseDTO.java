package org.frangosInfinity.application.module.produto.response;

public class CategoriaResponseDTO {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final int ordemExibicao;

    public CategoriaResponseDTO(Long id, String nome, String descricao, int ordemExibicao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.ordemExibicao = ordemExibicao;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getOrdemExibicao() {
        return ordemExibicao;
    }
}
