package org.frangosInfinity.infrastructure.console.module.pedido.controller;

import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.core.service.module.pedido.SubPedidoService;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class SubPedidoController {

    static SubPedidoService service;

    {
        try {

            service = new SubPedidoService();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public SubPedido criarSubPedido(SubPedido subPedido){

        if(subPedido == null){
            throw new RuntimeException("Erro - SubProduto não pode ser vazio");
        }

        SubPedidoRequestDTO subPedidoRequestDTO = new SubPedidoRequestDTO(subPedido.getId(),
                subPedido.getPedidoHub(),subPedido.getClienteID(),subPedido.getDate(),subPedido.getStatus(),
                subPedido.getValorTotal(),subPedido.getTempo_em_minutos(), subPedido.getObsevacoes());

        SubPedidoResponseDTO subPedidoResponseDTO;

        subPedidoResponseDTO = service.criarSubPedido(subPedidoRequestDTO);

        SubPedido subPedido1 = new SubPedido(subPedidoResponseDTO.getPedidoHub(),subPedido.getClienteID(),
                subPedido.getDate(),subPedido.getStatus(),subPedido.getValorTotal(),subPedido.getTempo_em_minutos(),
                subPedido.getObsevacoes());

        return subPedido1;

    }

    public boolean confirmarSubpedido(Long id){

        boolean i;

        i = service.confirmarSubPedido(id);

        return i;

    }

    public StatusPedido consultarStatus(Long id){

        if(id >= 1){

            return service.consultarStatus(id);

        }

        throw new RuntimeException("Erro - id invalido");

    }

    public void cancelarSubpedido(Long id){

        if(id >= 1){

            service.cancelarSubPedido(id);

        }

        throw new RuntimeException("Erro - id invalido");
    }

    public ArrayList<SubPedido> listarHistorioCliente(Long id){

        if(id >= 1){

            ArrayList<SubPedido> subPedidos = service.listarHistoricoCliente(id);

            return subPedidos;
        }

        throw new RuntimeException("Erro - id invalido");

    }

    public void solicitarPagamento(Long id){

        if(id >= 1){

            service.solicitarPagamento(id);

        }

        throw new RuntimeException("Erro - id invalido");

    }

    public void atualizarStatusPreparo(Long id, StatusPedido statusPedido){

        if(id >= 1 && statusPedido != null){

            service.atualizarStatusPreparo(id,statusPedido);

        }

        throw new RuntimeException("Erro - valores invalidos");

    }

    public Double calcularTotal(SubPedido subPedido){

        if(subPedido != null){

            SubPedidoRequestDTO subPedidoRequestDTO = new SubPedidoRequestDTO(subPedido.getId(),
                    subPedido.getPedidoHub(),subPedido.getClienteID(),subPedido.getDate(),subPedido.getStatus(),
                    subPedido.getValorTotal(),subPedido.getTempo_em_minutos(), subPedido.getObsevacoes());

            Double valor;

            valor = service.calcularTotal(subPedidoRequestDTO);

            return valor;
        }

        throw new RuntimeException("Erro - valores invalidos");

    }


}
