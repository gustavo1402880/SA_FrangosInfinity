package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pedido.PedidoDAO;
import org.frangosInfinity.infrastructure.persistence.module.pedido.SubPedidoDAO;

import java.sql.SQLException;
import java.util.ArrayList;

public class PedidoService {

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

    //  + gerarLinkConvi te(pedidoId: Long): St ring
    //  + val idarConvi te(token: St ring): Boolean

    //  + l i starPedidosPorStatus(status: StatusPedido): Li st<Pedido>
    //  + l i starPedidosAt rasados(): Li st<Pedido>
    //  + priori zarPorTempo(): Li st<Pedido>
    //  + reordenarPedidos(cri terio: St ring): void
    //  + l i starPedidosApp(): Li st<Pedido>
    //  + regi st rarPedidoManual (cl iente: Cl iente, mesa: Mesa, i tens: Li st<I temPedido>): Pedido
    //  + modi f i carPedido(pedidoId: Long, i tens: Li st<I temPedido>): Pedido
    //  + cancelarPedido(pedidoId: Long, just i f i cat i va: St ring): Boolean
    //  + atual i zarStatus(pedidoId: Long, novoStatus: StatusPedido): void


    //public PedidoResponseDTO criarPedidoHub(PedidoRequestDTO pedidoRequestDTO, String clienteSessao){}

    public ArrayList<SubPedido> listarSubPedidos(){

        ArrayList<SubPedido> subPedidos;

        return subPedidos = subPedidoDAO.buscarSubPedido();
    }

    public ArrayList<Pedido> listarPedidosPendestes(){

        ArrayList<Pedido> pedidos;

        return pedidos = pedidoDAO.listarPedidosPendentes();
    }

    public ArrayList<Pedido> listarPedidoPorStatus(int id){


        if(id == 1 || id == 2 || id == 3 || id == 4 || id == 5 || id ==)

    }


}
