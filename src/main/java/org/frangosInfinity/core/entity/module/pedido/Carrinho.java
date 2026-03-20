package org.frangosInfinity.core.entity.module.pedido;
import jakarta.mail.FetchProfile;
import jakarta.persistence.Id;
import org.frangosInfinity.core.enums.StatusPedido;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@RedisHash(value = "carrinho", timeToLive = 3600)
public class Carrinho implements Serializable
{
    @Id
    private String id;

    @Indexed
    private String sessaoId;

    private Long mesaId;

    private Long clienteId;

    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    private ArrayList<ItemCarrinho> itens;

    public Carrinho()
    {
        this.id = UUID.randomUUID().toString();
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Carrinho(String sessaoId, Long mesaId)
    {
        this();
        this.sessaoId = sessaoId;
        this.mesaId = mesaId;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSessaoId()
    {
        return sessaoId;
    }

    public void setSessaoId(String sessaoId)
    {
        this.sessaoId = sessaoId;
    }

    public Long getMesaId()
    {
        return mesaId;
    }

    public void setMesaId(Long mesaId)
    {
        this.mesaId = mesaId;
    }

    public Long getClienteId()
    {
        return clienteId;
    }

    public void setClienteId(Long clienteId)
    {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataCriacao()
    {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao)
    {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao()
    {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao)
    {
        this.dataAtualizacao = dataAtualizacao;
    }

    public ArrayList<ItemCarrinho> getItens()
    {
        return itens;
    }

    public void setItens(ArrayList<ItemCarrinho> itens)
    {
        this.itens = itens;
    }

    public static class ItemCarrinho implements Serializable
    {
        private Long produtoId;
        private String nome;
        private Integer quantidade;
        private Double precoUnitario;
        private String observacao;
        private Integer tempoPreparoEstimado;

        public ItemCarrinho() {}

        public ItemCarrinho(Long produtoId, String nome, Integer quantidade, Double preUnitario, Integer tempoPreparoEstimado)
        {
            this.produtoId = produtoId;
            this.nome = nome;
            this.quantidade = quantidade;
            this.precoUnitario = preUnitario;
            this.tempoPreparoEstimado = tempoPreparoEstimado;


        }

        public Long getProdutoId() {
            return produtoId;
        }

        public void setProdutoId(Long produtoId) {
            this.produtoId = produtoId;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Integer getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(Integer quantidade) {
            this.quantidade = quantidade;
        }

        public Double getPrecoUnitario() {
            return precoUnitario;
        }

        public void setPrecoUnitario(Double precoUnitario) {
            this.precoUnitario = precoUnitario;
        }

        public String getObservacao() {
            return observacao;
        }

        public void setObservacao(String observacao) {
            this.observacao = observacao;
        }

        public Integer getTempoPreparoEstimado() {
            return tempoPreparoEstimado;
        }

        public void setTempoPreparoEstimado(Integer tempoPreparoEstimado) {
            this.tempoPreparoEstimado = tempoPreparoEstimado;
        }

        public Double getSubTotal()
        {
            return quantidade * precoUnitario;
        }
    }

    public void adicionarItem(ItemCarrinho item)
    {
        for (ItemCarrinho i : itens)
        {
            if (i.getProdutoId().equals(item.getProdutoId()) &&
                    (i.getObservacao() == null ? item.getObservacao() == null :
                            i.getObservacao().equals(item.getObservacao())))
            {
                i.setQuantidade(i.getQuantidade() + item.getQuantidade());
                this.dataAtualizacao = LocalDateTime.now();
                return;
            }
        }
        itens.add(item);
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void removerItem(int index)
    {
        if (index >= 0 && index < itens.size())
        {
            itens.remove(index);
            this.dataAtualizacao = LocalDateTime.now();
        }
    }

    public void atualizarQuantidade(int index, int quantidade)
    {
        if (index >= 0 && index < itens.size())
        {
            ItemCarrinho item = itens.get(index);
            if (quantidade <= 0)
            {
                itens.remove(index);
            }
            this.dataAtualizacao = LocalDateTime.now();
        }
    }

    public Double getValorTotal()
    {
        return itens.stream()
                .mapToDouble(ItemCarrinho::getSubTotal)
                .sum();
    }

    public Integer getTotalItens()
    {
        return itens.stream()
                .mapToInt(ItemCarrinho::getQuantidade)
                .sum();
    }

    public SubPedido converterParaSubPedido(Pedido pedido, Long clienteId)
    {
        SubPedido subPedido = new SubPedido(pedido, clienteId);
        subPedido.setObsevacoes("Pedido via autoatendimento");

        for (ItemCarrinho itemCarrinho : itens)
        {
            ItemPedido item = new ItemPedido(
                    itemCarrinho.getProdutoId(),
                    itemCarrinho.getQuantidade(),
                    itemCarrinho.getPrecoUnitario(),
                    itemCarrinho.getTempoPreparoEstimado()
                    );
            item.setObservacao(itemCarrinho.getObservacao());
            subPedido.adicionarItem(item);
        }
        return subPedido;
    }

    public Boolean isExpirado()
    {
        return LocalDateTime.now().isAfter(dataAtualizacao.plusHours(1));
    }
}
