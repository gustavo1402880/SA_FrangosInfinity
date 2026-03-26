package org.frangosInfinity.core.service.module.pedido;
import org.frangosInfinity.application.module.pedido.request.PedidoRequestDTO;
import org.frangosInfinity.application.module.pedido.response.PedidoResponseDTO;
import org.frangosInfinity.core.entity.module.pedido.Pedido;
import org.frangosInfinity.core.entity.module.pedido.SubPedido;
import org.frangosInfinity.core.enums.StatusPedido;
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

    public PedidoResponseDTO registrarPedidoManual(SubPedido subPedido){


        return new PedidoResponseDTO(pedido.getId(),pedido.getNumeroPedido(),pedido.getDataHora(),pedido.getStatus(),pedido.getMesa_id(),pedido.getAtendente_id(),pedido.getTipo());

    }

    public boolean validarConvite(String link, Long id_hub){

        String urlBase = "https://FroangosInfinity.com/convite/";
        urlBase = urlBase + id_hub;

        if(link.equals(urlBase)){
            return true;
        }

        return false;
    }

    public String gerarLinkConvite(Long id){

        if(id >= 1) {
            String urlBase = "https://FrangosInfinity.com/convite/";
            return urlBase + id;
        }

        throw new IllegalArgumentException("Erro - valor de ID invalido");

    }

    public PedidoResponseDTO criarPedidoHub(Pedido pedido){

        pedidoDAO.adicionarPedido(pedido);

        return new PedidoResponseDTO(pedido.getId(),pedido.getNumeroPedido(),pedido.getDataHora(),pedido.getStatus(),pedido.getMesa_id(),pedido.getAtendente_id(),pedido.getTipo());

    }

    public ArrayList<SubPedido> listarSubPedidos(){

        ArrayList<SubPedido> subPedidos;

        return subPedidos = subPedidoDAO.buscarSubPedido();
    }

    public ArrayList<Pedido> listarPedidosPendestes(){

        ArrayList<Pedido> pedidos;

        return pedidos = pedidoDAO.listarPedidosPendentes();
    }

    public ArrayList<Pedido> listarPedidoPorStatus(int id){


        if(id == 1 || id == 2 || id == 3 || id == 4 || id == 5 || id == 6){

            ArrayList<Pedido> pedidos;
            String query = "";

            switch (id){
            case 1 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 1";
            case 2 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 2";
            case 3 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 3";
            case 4 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 4";
            case 5 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 5";
            case 6 -> query = "select * from pedido\\n\" +\n" +
                    "                \"        where status_id = 6";
            }

            return pedidos = pedidoDAO.listarPedidosPorStatus(query);

        }

        throw new IllegalArgumentException("Erro - o ID está invalido");
    }

    public ArrayList<Pedido> listarPedidoPorTempo(){

        ArrayList<Pedido> pedidos;

        return pedidos = pedidoDAO.listarPedidoPorTempo();

    }

    public void atualizarStatusPedido(Long id, StatusPedido statusPedido){

        if(id >= 1 && statusPedido != null){

            pedidoDAO.AtualizarPedido(id,statusPedido.getCodigo());

        }

        throw new IllegalArgumentException("Erro ao atualizar pedido");

    }

    public void cancelarPedido(Long id){

        if(id >= 1 ){

            pedidoDAO.AtualizarPedido(id,5);

        }

        throw new IllegalArgumentException("Erro ao atualizar pedido");

    }

    public Double calcularTotal(Long id){

        ArrayList<SubPedido> subPedidos = subPedidoDAO.buscarSubPedido();

        Double total = 0.0;

        for(SubPedido subPedido : subPedidos){
            if (subPedido.getPedidoHub().getId().equals(id)) {
                total += subPedido.getValorTotal();
            }
        }

        return total;
    }

}
