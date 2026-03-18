package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.ItemPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CarrinhoService {

    public Carrinho adicionarItem(ItemPedido itemPedido, Carrinho carrinho) throws Exception
    {

        ArrayList<ItemPedido> pedidos;
        Double valortotal;

        if (itemPedido != null) {

            pedidos = carrinho.getItens();

            pedidos.add(itemPedido);

            valortotal = carrinho.getValorTotal();

            valortotal += itemPedido.getSubTotal();

            carrinho.setItens(pedidos);

            return carrinho;

        }else {

            throw new Exception("Erro ao adicionar item no carrinho");

        }
    }

    public Carrinho removerItem(ItemPedido itemPedido, Carrinho carrinho) throws Exception
    {

        ArrayList<ItemPedido> pedidos;

        if (itemPedido != null) {

            pedidos = carrinho.getItens();

            pedidos.remove(itemPedido);

            carrinho.setItens(pedidos);

            return carrinho;

        }else {

            throw new Exception("Erro ao remover produto");

        }
    }

    public Carrinho alterarQuantidade(ItemPedido itemPedido, int quantidade, Carrinho carrinho) throws Exception{

        ArrayList<ItemPedido> pedidos;

        try{

            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(ConnectionFactory.getConnection());

            if (itemPedido != null && quantidade > 0) {

                pedidos = carrinho.getItens();

                pedidos.remove(itemPedido);

                ItemPedido itemPedido1 = itemPedidoDAO.updateQuantidade(itemPedido,quantidade);

                pedidos.add(itemPedido1);

                carrinho.setItens(pedidos);

                return carrinho;

            }

        } catch (SQLException e) {

            throw new Exception("Erro ao alterar quantidade");

        }

        throw new Exception("Erro ao alterar quantidade");

    }

    public Double calcularTotal(Carrinho carrinho) throws Exception{


        if(carrinho != null){

            return carrinho.calcularTotal();

        }else {

            throw new Exception("Erro ao calcular total");

        }
    }

    public void limpar(Carrinho carrinho) throws Exception{

        if (carrinho != null) {
            ArrayList<ItemPedido> pedidos = carrinho.getItens();

            pedidos.clear();

            carrinho.setItens(pedidos);

            return;
        }

        throw new Exception("Erro ao limpar o carrinho");
    }

    public SubPedido converterParaSubPedido(Carrinho carrinho)throws Exception{

        if(carrinho != null) {

            SubPedido subPedido = new SubPedido();

            // FAZER

        }

        throw new Exception("Erro ao converter para SubPedido");

    }

}
