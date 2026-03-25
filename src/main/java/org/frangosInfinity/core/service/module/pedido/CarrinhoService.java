package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.ItemPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class CarrinhoService {

    public CarrinhoResponseDTO adicionarItem(ItemPedidoRequestDTO itemPedidoRequestDTO, CarrinhoRequestDTO carrinhoRequestDTO)
    {

        ArrayList<ItemPedido> pedidos;
        Double valortotal = 0.0;
        ItemPedido itemPedido = new ItemPedido(itemPedidoRequestDTO.getId_ItemPedido(),
                itemPedidoRequestDTO.getSubPedidoID(),
                itemPedidoRequestDTO.getProdutoid(),
                itemPedidoRequestDTO.getQuantidade(),
                itemPedidoRequestDTO.getPrecoUnitario(),
                itemPedidoRequestDTO.getObservacao(),
                itemPedidoRequestDTO.getSubTotal());

        if (itemPedidoRequestDTO != null) {

            pedidos = carrinhoRequestDTO.getItens();

            pedidos.add(itemPedido);


            for(ItemPedido i : pedidos){
                valortotal += i.getSubTotal();
            }

            CarrinhoResponseDTO carrinho = new CarrinhoResponseDTO(carrinhoRequestDTO.getId_carrinho(),carrinhoRequestDTO.getCliente_id(),
                    carrinhoRequestDTO.getDataCriacao(),pedidos,valortotal);

            return carrinho;

        }else {

            throw new RuntimeException("Erro ao adicionar item no carrinho");

        }
    }

    public CarrinhoResponseDTO removerItem(ItemPedidoRequestDTO itemPedidoRequestDTO, CarrinhoRequestDTO carrinhoRequestDTO)
    {

        ArrayList<ItemPedido> pedidos;

        if (itemPedidoRequestDTO != null) {

            pedidos = carrinhoRequestDTO.getItens();

            pedidos.removeIf(item -> item.getId_ItemPedido().equals(itemPedidoRequestDTO.getId_ItemPedido()));

            Double valortotal = 0.0;

            for(ItemPedido i : pedidos){
                valortotal += i.getSubTotal();
            }

            CarrinhoResponseDTO carrinho = new CarrinhoResponseDTO(carrinhoRequestDTO.getId_carrinho(),carrinhoRequestDTO.getCliente_id(),
                    carrinhoRequestDTO.getDataCriacao(),pedidos,valortotal);

            return carrinho;

        }else {

            throw new RuntimeException("Erro ao remover produto");

        }
    }

    public CarrinhoResponseDTO alterarQuantidade(ItemPedidoRequestDTO itemPedidoRequestDTO, int quantidade, CarrinhoRequestDTO carrinhoRequestDTO) {

        ArrayList<ItemPedido> pedidos;
        ItemPedido itemPedido = new ItemPedido(itemPedidoRequestDTO.getId_ItemPedido(),
                itemPedidoRequestDTO.getSubPedidoID(),
                itemPedidoRequestDTO.getProdutoid(),
                itemPedidoRequestDTO.getQuantidade(),
                itemPedidoRequestDTO.getPrecoUnitario(),
                itemPedidoRequestDTO.getObservacao(),
                itemPedidoRequestDTO.getSubTotal());

        try{

            ItemPedidoDAO itemPedidoDAO = new ItemPedidoDAO(ConnectionFactory.getConnection());

            if (itemPedidoRequestDTO != null && quantidade > 0) {

                pedidos = carrinhoRequestDTO.getItens();

                pedidos.removeIf(item -> item.getId_ItemPedido().equals(itemPedidoRequestDTO.getId_ItemPedido()));

                ItemPedido itemPedido1 = itemPedidoDAO.updateQuantidade(itemPedido,quantidade);

                pedidos.add(itemPedido1);

                Double valortotal = 0.0;

                for(ItemPedido i : pedidos){
                    valortotal += i.getSubTotal();
                }

                CarrinhoResponseDTO carrinho = new CarrinhoResponseDTO(carrinhoRequestDTO.getId_carrinho(),carrinhoRequestDTO.getCliente_id(),
                        carrinhoRequestDTO.getDataCriacao(),pedidos,valortotal);

                return carrinho;

            }

        } catch (SQLException e) {

            throw new RuntimeException("Erro ao alterar quantidade");

        }

        throw new RuntimeException("Erro ao alterar quantidade");

    }

    public Double calcularTotal(CarrinhoRequestDTO carrinho)  {

        ArrayList<ItemPedido> pedidos = carrinho.getItens();

        if(carrinho != null){

            Double valortotal = 0.0;

            for(ItemPedido i : pedidos){
                valortotal += i.getSubTotal();
            }

            return valortotal;

        }else {

            throw new RuntimeException("Erro ao calcular total");

        }
    }

    public CarrinhoResponseDTO limpar(CarrinhoRequestDTO carrinhoRequestDTO)  {

        if (carrinhoRequestDTO != null) {
            ArrayList<ItemPedido> pedidos = carrinhoRequestDTO.getItens();

            pedidos.clear();

            Double valortotal = 0.0;

            for(ItemPedido i : pedidos){
                valortotal += i.getSubTotal();
            }

            CarrinhoResponseDTO carrinho = new CarrinhoResponseDTO(carrinhoRequestDTO.getId_carrinho(),carrinhoRequestDTO.getCliente_id(),
                    carrinhoRequestDTO.getDataCriacao(),pedidos,valortotal);


            return carrinho;
        }

        throw new RuntimeException("Erro ao limpar o carrinho");
    }

    public SubPedidoResponseDTO converterParaSubPedido(CarrinhoRequestDTO carrinhoRequestDTO) {

        if(carrinhoRequestDTO != null) {

            SubPedido subPedido = new SubPedido();

            // FAZER

        }

        throw new RuntimeException("Erro ao converter para SubPedido");

    }

}
