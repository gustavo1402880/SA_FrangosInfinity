package org.frangosInfinity.application.module.pedido.request;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import java.sql.Date;
import java.util.ArrayList;

public class CarrinhoRequestDTO {

    private final Long id_carrinho;
    private final String cliente_id;
    private final Date dataCriacao;
    private final ArrayList<ItemPedido> itens;

    public CarrinhoRequestDTO(Long id_carrinho, String cliente_id, Date dataCriacao, ArrayList<ItemPedido> itens){

        if(id_carrinho >= 1 && dataCriacao.after(Date.valueOf("1900-1-1")) && !itens.isEmpty()){
            this.id_carrinho = id_carrinho;
            this.cliente_id = cliente_id;
            this.dataCriacao = dataCriacao;
            this.itens = itens;
        }

        throw new IllegalArgumentException("Erro - valores de carrinho estão no formato incorreto");
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
}
