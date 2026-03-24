package org.frangosInfinity.infrastructure.console.module.pedido.controller;

import com.beust.ah.A;
import org.frangosInfinity.application.module.pedido.request.PedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.PedidoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.core.service.module.pedido.PedidoService;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class PedidoController {

    private PedidoService pedidoService;

    {
        try {
            pedidoService = new PedidoService();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Pedido registrarPedidoManual(Pedido pedido){

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(pedido.getNumeroPedido(),pedido.getDataHora(),
                pedido.getStatus(),pedido.getMesa_id(),pedido.getAtendente_id(),pedido.getTipo());


        PedidoResponseDTO pedidoResponseDTO;

        pedidoResponseDTO = pedidoService.registrarPedidoManual(pedidoRequestDTO);

        Pedido pedido1 = new Pedido(pedidoResponseDTO.getNumeroPedido(),pedidoResponseDTO.getDataHora(),
                pedidoResponseDTO.getStatus(),pedidoResponseDTO.getMesa_id(),pedidoResponseDTO.getAtendente_id(),
                pedidoResponseDTO.getTipo());

        return pedido1;

    }

    public boolean validarConvite(String link, Long id_hub){

        if(link != null && id_hub >= 1){

            return pedidoService.validarConvite(link,id_hub);

        }

        throw new RuntimeException("Erro - Valor invalido");

    }

    public String gerarLinkConvite(Long id){

        if(id >= 1){

            return pedidoService.gerarLinkConvite(id);

        }

        throw new RuntimeException("Erro - id invalido");

    }

    public Pedido criarPedidoHub(Pedido pedido){

        if(pedido == null){
            throw new RuntimeException("Erro - pedido não pode ser vazio");
        }

        PedidoRequestDTO pedidoRequestDTO = new PedidoRequestDTO(pedido.getNumeroPedido(),pedido.getDataHora(),
                pedido.getStatus(),pedido.getMesa_id(),pedido.getAtendente_id(),pedido.getTipo());

        PedidoResponseDTO pedidoResponseDTO;

        pedidoResponseDTO = pedidoService.criarPedidoHub(pedidoRequestDTO);

        Pedido pedido1 = new Pedido(pedidoResponseDTO.getNumeroPedido(),pedidoResponseDTO.getDataHora(),
                pedidoResponseDTO.getStatus(),pedidoResponseDTO.getMesa_id(),pedidoResponseDTO.getAtendente_id(),
                pedidoResponseDTO.getTipo());

        return pedido1;

    }

    public ArrayList<SubPedido> listarSubPedidos(){

        return pedidoService.listarSubPedidos();
    }

    public ArrayList<Pedido> listarPedidosPendentes(){

        return pedidoService.listarPedidosPendestes();
    }

    public ArrayList<Pedido> listarPedidosPorStatus(int id){

        if(id >= 1 && id <= 6){

            return pedidoService.listarPedidoPorStatus(id);

        }

        throw new RuntimeException("Erro - id invalido");
    }

    public ArrayList<Pedido> listarPedidoPorTempo(){

        return pedidoService.listarPedidoPorTempo();

    }

    public void atualizarStatusPedido(Long id, StatusPedido statusPedido){

        pedidoService.atualizarStatusPedido(id,statusPedido);

    }

    public void cancelarPedido(Long id){

        pedidoService.cancelarPedido(id);
    }

    public Double calcularTotal(Long id){

        return pedidoService.calcularTotal(id);
    }

}
