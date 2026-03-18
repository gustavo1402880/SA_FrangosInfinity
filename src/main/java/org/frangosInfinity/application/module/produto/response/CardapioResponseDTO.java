package org.frangosInfinity.application.module.produto.response;

import java.sql.Date;

public class CardapioResponseDTO {

    private final Long id;
    private final int versao;
    private final Date dataAtualizacao;

    public CardapioResponseDTO(Long id, int versao, Date dataAtualizacao) {
        this.id = id;
        this.versao = versao;
        this.dataAtualizacao = dataAtualizacao;
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
