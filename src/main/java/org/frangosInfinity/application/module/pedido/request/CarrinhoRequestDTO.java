package org.frangosInfinity.application.module.pedido.request;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import java.sql.Date;
import java.util.ArrayList;

public class CarrinhoRequestDTO {

    private Long id_carrinho;
    private String cliente_id;
    private Date dataCriacao;
    private ArrayList<ItemPedido> itens;

    public CarrinhoRequestDTO() {};

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

    public void setId_carrinho(Long id_carrinho) {
        this.id_carrinho = id_carrinho;
    }

    public String getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(String cliente_id) {
        this.cliente_id = cliente_id;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public ArrayList<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(ArrayList<ItemPedido> itens) {
        this.itens = itens;
    }
}
