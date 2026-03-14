package org.frangosInfinity.core.entity.module.produto;

import java.sql.Date;
import java.time.LocalDateTime;

public class Cardapio {

    // Atributos

    private Long id;
    private int versao;
    private Date dataAtualizacao;

    // Contrutores

    public Cardapio() {}

    public Cardapio(Long id, int versao, java.sql.Date dataAtualizacao) {
        this.id = id;
        this.versao = versao;
        this.dataAtualizacao = dataAtualizacao;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public java.sql.Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
