package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarrinhoResponseDTO
{
    private String id;
    private String sessaoId;
    private Long mesaId;
    private Long clienteId;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private Double valorTotal;
    private Integer totalItens;
    private List<ItemCarrinhoDTO> itens;
    private Boolean sucesso;
    private String mensagem;

    public CarrinhoResponseDTO() {}

    public CarrinhoResponseDTO fromEntity(Carrinho carrinho)
    {
        CarrinhoResponseDTO response = new CarrinhoResponseDTO();
        response.setId(carrinho.getId());
        response.setSessaoId(carrinho.getSessaoId());
        response.setMesaId(carrinho.getMesaId());
        response.setClienteId(carrinho.getClienteId());
        response.setDataCriacao(carrinho.getDataCriacao());
        response.setDataAtualizacao(carrinho.getDataAtualizacao());

        if (carrinho.getItens() != null && !carrinho.getItens().isEmpty())
        {
            response.setItens(carrinho.getItens().stream()
                    .map(ItemCarrinhoDTO::fromEntity)
                    .collect(Collectors.toList()));
        }

        response.setValorTotal(carrinho.getValorTotal());
        response.setTotalItens(carrinho.getTotalItens());
        response.setSucesso(true);
        return response;
    }

    public static CarrinhoResponseDTO erro(String mensagem)
    {
        CarrinhoResponseDTO response = new CarrinhoResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }

    public static class ItemCarrinhoDTO
    {
        private Long produtoId;
        private String nome;
        private Integer quantidade;
        private Double precoUnitario;
        private Double subTotal;
        private String observacao;

        public static ItemCarrinhoDTO fromEntity(Carrinho.ItemCarrinho item)
        {
            ItemCarrinhoDTO response = new ItemCarrinhoDTO();
            response.setProdutoId(item.getProdutoId());
            response.setNome(item.getNome());
            response.setQuantidade(item.getQuantidade());
            response.setPrecoUnitario(item.getPrecoUnitario());
            response.setSubTotal(item.getSubTotal());
            response.setObservacao(item.getObservacao());
            return response;
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

        public Double getSubTotal() {
            return subTotal;
        }

        public void setSubTotal(Double subTotal) {
            this.subTotal = subTotal;
        }

        public String getObservacao() {
            return observacao;
        }

        public void setObservacao(String observacao) {
            this.observacao = observacao;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSessaoId() {
        return sessaoId;
    }

    public void setSessaoId(String sessaoId) {
        this.sessaoId = sessaoId;
    }

    public Long getMesaId() {
        return mesaId;
    }

    public void setMesaId(Long mesaId) {
        this.mesaId = mesaId;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Integer getTotalItens() {
        return totalItens;
    }

    public void setTotalItens(Integer totalItens) {
        this.totalItens = totalItens;
    }

    public List<ItemCarrinhoDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemCarrinhoDTO> itens) {
        this.itens = itens;
    }

    public Boolean getSucesso() {
        return sucesso;
    }

    public void setSucesso(Boolean sucesso) {
        this.sucesso = sucesso;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
