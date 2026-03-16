package org.frangosInfinity.application.module.produto.request;

public class CategoriaRequestDTO {

    private final Long id;
    private final String nome;
    private final String descricao;
    private final int ordemExibicao;

    public CategoriaRequestDTO(Long id, String nome, String descricao, int ordemExibicao) {
        if(id >= 1 && !nome.isEmpty() && !descricao.isEmpty() && ordemExibicao >= 1) {
            this.id = id;
            this.nome = nome;
            this.descricao = descricao;
            this.ordemExibicao = ordemExibicao;
        }

        throw new IllegalArgumentException("Erro - os dados de categoria estão incarretos");

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
