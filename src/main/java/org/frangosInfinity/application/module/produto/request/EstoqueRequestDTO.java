package org.frangosInfinity.application.module.produto.request;

import org.frangosInfinity.core.entity.module.produto.Produto;

public class EstoqueRequestDTO {

    private final Long id;
    private final Produto produto;
    private final int quantidadeAtual;
    private final int quantidadeMinima;
    private final int quantidadeMaxima;

    public EstoqueRequestDTO(Long id, Produto produto, int quantidadeAtual, int quantidadeMinima, int quantidadeMaxima) {

        if(id >= 1 && produto != null && quantidadeAtual >= 1 && quantidadeMaxima >= 1 && quantidadeMinima >= 1) {
            this.id = id;
            this.produto = produto;
            this.quantidadeAtual = quantidadeAtual;
            this.quantidadeMinima = quantidadeMinima;
            this.quantidadeMaxima = quantidadeMaxima;
        }

        throw new IllegalArgumentException("Erro - os dados de estoque estão incorretos");

    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public int getQuantidadeMinima() {
        return quantidadeMinima;
    }

    public int getQuantidadeMaxima() {
        return quantidadeMaxima;
    }
}
