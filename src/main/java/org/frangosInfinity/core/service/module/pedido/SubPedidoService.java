package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.SubPedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.SubPedidoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class SubPedidoService {

    static SubPedidoDAO subPedidoDAO;

    private PedidoDAO pedidoDAO;

    {
        try {
            pedidoDAO = new PedidoDAO(ConnectionFactory.getConnection());
            subPedidoDAO = new SubPedidoDAO(ConnectionFactory.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public SubPedidoResponseDTO criarSubPedido(SubPedidoRequestDTO subPedidoRequestDTO ){

        SubPedido subPedido = new SubPedido();

        subPedidoDAO.adicionarSubPedido(subPedido);

        return new SubPedidoResponseDTO(subPedidoRequestDTO.getClienteID(),subPedidoRequestDTO.getPedidoHub(),subPedidoRequestDTO.getClienteID(),subPedidoRequestDTO.getDate(),subPedidoRequestDTO.getStatus(),subPedidoRequestDTO.getValorTotal(),subPedidoRequestDTO.getTempo_em_minutos(),subPedidoRequestDTO.getObsevacoes());

    }

    public boolean confirmarSubPedido(Long id){

        if(id >= 1) {

            SubPedido subPedido = subPedidoDAO.buscarPorId(id);

            if(subPedido)

            subPedidoDAO.AtualizarStatusPedido(id, 1);

            return true;
        }

        throw new RuntimeException("Erro - valor digitedo incorreto");

    }
    public StatusPedido consultarStatus(Long id){

        SubPedido subPedido = subPedidoDAO.buscarPorId(id);

        return subPedido.getStatus();
    }

    public void cancelarSubPedido(Long id ){

        subPedidoDAO.AtualizarStatusPedido(id,5);

    }

    public ArrayList<SubPedido> listarHistoricoCliente(Long id){


        ArrayList<SubPedido> subPedidos =  subPedidoDAO.buscarSubPedido();

        ArrayList<SubPedido> subPedidos1 = new ArrayList<>();

        for(SubPedido subPedido: subPedidos){

            if(subPedido.getClienteID().equals(id)){

                subPedidos1.add(subPedido);

            }
        }

        return subPedidos1;
    }

    public void solicitarPagamento(Long id ){

        subPedidoDAO.AtualizarStatusPedido(id,6);

    }

    public void atualizarStatusPreparo(Long id, StatusPedido statusPedido){

        subPedidoDAO.AtualizarStatusPedido(id,statusPedido.getCodigo());

    }

    public Double calcularTotal(SubPedidoRequestDTO subPedidoRequestDTO){

        return subPedidoRequestDTO.getValorTotal();

    }


}
