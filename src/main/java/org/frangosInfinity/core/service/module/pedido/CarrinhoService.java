package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.ItemPedidoRepository;

import java.sql.SQLException;
import java.util.ArrayList;

public class CarrinhoService {

    public CarrinhoResponseDTO adicionarItem(ItemPedidoRequestDTO itemPedidoRequestDTO, CarrinhoRequestDTO carrinhoRequestDTO) throws Exception
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

            throw new Exception("Erro ao adicionar item no carrinho");

        }
    }

    public CarrinhoResponseDTO removerItem(ItemPedidoRequestDTO itemPedidoRequestDTO, CarrinhoRequestDTO carrinhoRequestDTO) throws Exception
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

            throw new Exception("Erro ao remover produto");

        }
    }

    public CarrinhoResponseDTO alterarQuantidade(ItemPedidoRequestDTO itemPedidoRequestDTO, int quantidade, CarrinhoRequestDTO carrinhoRequestDTO) throws Exception{

        ArrayList<ItemPedido> pedidos;
        ItemPedido itemPedido = new ItemPedido(itemPedidoRequestDTO.getId_ItemPedido(),
                itemPedidoRequestDTO.getSubPedidoID(),
                itemPedidoRequestDTO.getProdutoid(),
                itemPedidoRequestDTO.getQuantidade(),
                itemPedidoRequestDTO.getPrecoUnitario(),
                itemPedidoRequestDTO.getObservacao(),
                itemPedidoRequestDTO.getSubTotal());

        try{

            ItemPedidoRepository itemPedidoRepository = new ItemPedidoRepository(ConnectionFactory.getConnection());

            if (itemPedidoRequestDTO != null && quantidade > 0) {

                pedidos = carrinhoRequestDTO.getItens();

                pedidos.removeIf(item -> item.getId_ItemPedido().equals(itemPedidoRequestDTO.getId_ItemPedido()));

                ItemPedido itemPedido1 = itemPedidoRepository.updateQuantidade(itemPedido,quantidade);

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

            throw new Exception("Erro ao alterar quantidade");

        }

        throw new Exception("Erro ao alterar quantidade");

    }

    public Double calcularTotal(CarrinhoRequestDTO carrinho) throws Exception{

        ArrayList<ItemPedido> pedidos = carrinho.getItens();

        if(carrinho != null){

            Double valortotal = 0.0;

            for(ItemPedido i : pedidos){
                valortotal += i.getSubTotal();
            }

            return valortotal;

        }else {

            throw new Exception("Erro ao calcular total");

        }
    }

    public CarrinhoResponseDTO limpar(CarrinhoRequestDTO carrinhoRequestDTO) throws Exception{

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
