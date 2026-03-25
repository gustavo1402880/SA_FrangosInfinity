package org.frangosInfinity.infrastructure.console.module.pedido.controller;

import org.frangosInfinity.application.module.pedido.request.CarrinhoRequestDTO;
import org.frangosInfinity.application.module.pedido.request.ItemPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.CarrinhoResponseDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.application.module.produto.request.CategoriaRequestDTO;
import org.frangosInfinity.core.entity.module.pedido.Carrinho;
import org.frangosInfinity.core.entity.module.pedido.ItemPedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.service.module.pedido.CarrinhoService;
import org.frangosInfinity.core.service.module.pedido.SubPedidoService;

public class CarrinhoController {

    static CarrinhoService carrinhoService;

    {
        try {

            carrinhoService = new CarrinhoService();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public Carrinho adicionarItem(ItemPedido itemPedido, Carrinho carrinho){

        if(itemPedido == null || carrinho == null){

            throw new RuntimeException("Erro - valores nulos");
        }

        ItemPedidoRequestDTO itemPedidoRequestDTO = new ItemPedidoRequestDTO(itemPedido.getId_ItemPedido(),
                itemPedido.getSubPedido(),itemPedido.getProduto(),itemPedido.getQuantidade(),itemPedido.getPrecoUnitario(),
                itemPedido.getObservacao(),itemPedido.calcularSubTotal());

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        CarrinhoResponseDTO carrinhoResponseDTO;

        carrinhoResponseDTO = carrinhoService.adicionarItem(itemPedidoRequestDTO,carrinhoRequestDTO);

        Carrinho carrinho1 = new Carrinho(carrinhoResponseDTO.getDataCriacao(),
                carrinhoResponseDTO.getCliente_id(),carrinhoResponseDTO.getItens(),
                carrinhoResponseDTO.getValorTotal());

        carrinho1.setId_carrinho(carrinhoResponseDTO.getId_carrinho());

        return carrinho1;

    }

    public Carrinho removerItem(ItemPedido itemPedido, Carrinho carrinho){

        if(itemPedido == null || carrinho == null){

            throw new RuntimeException("Erro - valores nulos");
        }

        ItemPedidoRequestDTO itemPedidoRequestDTO = new ItemPedidoRequestDTO(itemPedido.getId_ItemPedido(),
                itemPedido.getSubPedido(),itemPedido.getProduto(),itemPedido.getQuantidade(),itemPedido.getPrecoUnitario(),
                itemPedido.getObservacao(),itemPedido.calcularSubTotal());

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        CarrinhoResponseDTO carrinhoResponseDTO;

        carrinhoResponseDTO = carrinhoService.removerItem(itemPedidoRequestDTO,carrinhoRequestDTO);

        Carrinho carrinho1 = new Carrinho(carrinhoResponseDTO.getDataCriacao(),
                carrinhoResponseDTO.getCliente_id(),carrinhoResponseDTO.getItens(),
                carrinhoResponseDTO.getValorTotal());

        carrinho1.setId_carrinho(carrinhoResponseDTO.getId_carrinho());

        return carrinho1;

    }

    public Carrinho alterarQuantidade(ItemPedido itemPedido,int quantidade ,Carrinho carrinho){

        if(itemPedido == null || carrinho == null || quantidade <= 0){

            throw new RuntimeException("Erro - valores incorretos");
        }

        ItemPedidoRequestDTO itemPedidoRequestDTO = new ItemPedidoRequestDTO(itemPedido.getId_ItemPedido(),
                itemPedido.getSubPedido(),itemPedido.getProduto(),itemPedido.getQuantidade(),itemPedido.getPrecoUnitario(),
                itemPedido.getObservacao(),itemPedido.calcularSubTotal());

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        CarrinhoResponseDTO carrinhoResponseDTO;

        carrinhoResponseDTO = carrinhoService.alterarQuantidade(itemPedidoRequestDTO,quantidade,carrinhoRequestDTO);

        Carrinho carrinho1 = new Carrinho(carrinhoResponseDTO.getDataCriacao(),
                carrinhoResponseDTO.getCliente_id(),carrinhoResponseDTO.getItens(),
                carrinhoResponseDTO.getValorTotal());

        carrinho1.setId_carrinho(carrinhoResponseDTO.getId_carrinho());

        return carrinho1;

    }

    public Double calcularTotal(Carrinho carrinho){

        if( carrinho == null){

            throw new RuntimeException("Erro - valor nulo");
        }

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        Double valor;

        valor = carrinhoService.calcularTotal(carrinhoRequestDTO);

        return valor;

    }

    public Carrinho limpar(Carrinho carrinho){

        if( carrinho == null){

            throw new RuntimeException("Erro - valor nulo");
        }

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        CarrinhoResponseDTO carrinhoResponseDTO;

        carrinhoResponseDTO = carrinhoService.limpar(carrinhoRequestDTO);

        Carrinho carrinho1 = new Carrinho(carrinhoResponseDTO.getDataCriacao(),
                carrinhoResponseDTO.getCliente_id(),carrinhoResponseDTO.getItens(),
                carrinhoResponseDTO.getValorTotal());

        carrinho1.setId_carrinho(carrinhoResponseDTO.getId_carrinho());

        return carrinho1;
    }

    public SubPedido converterParaSubPedido(Carrinho carrinho){

        if(carrinho == null){

            throw new RuntimeException("Erro - valor invalido");
        }

        CarrinhoRequestDTO carrinhoRequestDTO = new CarrinhoRequestDTO(carrinho.getId_carrinho(),carrinho.getCliente_id(),
                carrinho.getDataCriacao(),carrinho.getItens());

        SubPedidoResponseDTO subPedidoResponseDTO;

        subPedidoResponseDTO = carrinhoService.converterParaSubPedido(carrinhoRequestDTO);

        SubPedido subPedido;

        subPedido = new SubPedido(subPedidoResponseDTO.getPedidoHub(),subPedidoResponseDTO.getClienteID(),
                subPedidoResponseDTO.getDate(),subPedidoResponseDTO.getStatus(),subPedidoResponseDTO.getValorTotal(),
                subPedidoResponseDTO.getTempo_em_minutos(),subPedidoResponseDTO.getObsevacoes());

        return subPedido;
    }
}
