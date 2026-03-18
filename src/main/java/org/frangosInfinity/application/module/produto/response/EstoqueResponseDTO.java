package org.frangosInfinity.application.module.produto.response;

import org.frangosInfinity.core.entity.module.produto.Produto;

public class EstoqueResponseDTO {

    private final Long id;
    private final Produto produto;
    private final int quantidadeAtual;
    private final int quantidadeMinima;
    private final int quantidadeMaxima;

    public EstoqueResponseDTO(Long id, Produto produto, int quantidadeAtual, int quantidadeMinima, int quantidadeMaxima) {
        this.id = id;
        this.produto = produto;
        this.quantidadeAtual = quantidadeAtual;
        this.quantidadeMinima = quantidadeMinima;
        this.quantidadeMaxima = quantidadeMaxima;
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
