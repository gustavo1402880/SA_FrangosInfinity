package org.frangosInfinity.core.entity.module.produto;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cardapio")
public class Cardapio
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer versao;

    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    public Cardapio()
    {
        this.versao = 1;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Cardapio(Integer versao)
    {
        this.versao = versao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Integer getVersao()
    {
        return versao;
    }

    public void setVersao(Integer versao)
    {
        this.versao = versao;
    }

    public LocalDateTime getDataAtualizacao()
    {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao)
    {
        this.dataAtualizacao = dataAtualizacao;
    }

    public void atualizarVersao()
    {
        this.versao++;
        this.dataAtualizacao = LocalDateTime.now();
    }
}
