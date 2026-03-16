package org.frangosInfinity.application.module.produto.request;

import java.sql.Date;

public class CardapioRequestDTO {

    private final Long id;
    private final int versao;
    private final Date dataAtualizacao;

    public CardapioRequestDTO(Long id, int versao, Date dataAtualizacao) {
        if(id >= 1 && versao >= 1 && dataAtualizacao.after(Date.valueOf("1900-1-1"))) {
            this.id = id;
            this.versao = versao;
            this.dataAtualizacao = dataAtualizacao;
        }

        throw new IllegalArgumentException("Erro - os dados de cardapio estão em formato incorreto");

    }

    public Long getId() {
        return id;
    }

    public int getVersao() {
        return versao;
    }

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }
}
