package org.frangosInfinity.core.entity.module.pedido;
import org.frangosInfinity.core.enums.StatusPedido;
import java.sql.Date;
import java.util.ArrayList;


public class Carrinho {

    // Atributos

    private Long id_carrinho;
    private Long cliente_id;
    private Date dataCriacao;
    private ArrayList<ItemPedido> itens;
    private Double valorTotal;

    // Contrutores



    public Carrinho( Date dataCriacao,Long cliente_id, ArrayList<ItemPedido> itens, Double valorTotal) {
        this.cliente_id = cliente_id;
        this.dataCriacao = dataCriacao;
        this.itens = itens;
        this.valorTotal = valorTotal;
    }

    // Getters & Setters


    public Long getId_carrinho() {
        return id_carrinho;
    }

    public void setId_carrinho(Long id_carrinho) {
        this.id_carrinho = id_carrinho;
    }

    public java.sql.Date getDataCriacao() {
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

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Long getCliente_id() {
        return cliente_id;
    }

    public void setCliente_id(Long cliente_id) {
        this.cliente_id = cliente_id;
    }

    // Metodos

    public void adicionarItem(ItemPedido itemPedido){
        itens.add(itemPedido);
    }

    public void removerItem(ItemPedido itemPedido){
        itens.remove(itemPedido);
    }

    public void alterarQuantidade(Long itenID, int quantidade){

        for(ItemPedido i : itens){
            if(itenID.equals(i.getId_ItemPedido())){
                i.setQuantidade(quantidade);
                itens.set( itens.indexOf(i), i);
            }
        }
    }

    public ArrayList<ItemPedido> verItens(){

    return itens;
    }

    public double calcularTotal(){

    double total = 0;

    for(ItemPedido i : itens){
        total += i.getSubTotal();
    }

    return total;

    }

    public void limpar(){

    itens.clear();

    }

    public SubPedido converterParaSubPedido(Pedido pedidoHubId, String observacao, int tempo){

        return new SubPedido(pedidoHubId,cliente_id,dataCriacao, StatusPedido.CONFIMADO,calcularTotal(), tempo,observacao);

    }

}
