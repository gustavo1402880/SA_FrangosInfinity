package org.frangosInfinity.application.module.pedido.response;

import org.frangosInfinity.core.entity.module.pedido.ItemPedido;

import java.sql.Date;
import java.util.ArrayList;

public class CarrinhoResponseDTO {

    private final Long id_carrinho;
    private final String cliente_id;
    private final Date dataCriacao;
    private final ArrayList<ItemPedido> itens;
    private final Double valorTotal;

    public CarrinhoResponseDTO(Long id_carrinho, String cliente_id, Date dataCriacao, ArrayList<ItemPedido> itens, Double valorTotal) {
        this.id_carrinho = id_carrinho;
        this.cliente_id = cliente_id;
        this.dataCriacao = dataCriacao;
        this.itens = itens;
        this.valorTotal = valorTotal;
    }

    public Long getId_carrinho() {
        return id_carrinho;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public ArrayList<ItemPedido> getItens() {
        return itens;
    }

    public Double getValorTotal() {
        return valorTotal;
    }
}
