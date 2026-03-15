package org.frangosInfinity.core.service.module.pagamento;

import org.frangosInfinity.application.module.pagamento.response.ComprovanteResponseDTO;
import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.pagamento.ComprovanteDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ComprovanteService
{
    private Boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    public Comprovante buscarPorId(Long id)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if (!validarId(id))
            {
                return null;
            }

            ComprovanteDAO comprovanteDAO = new ComprovanteDAO(connection);
            return comprovanteDAO.buscarPorId(id).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public Comprovante buscarporPagamentoId(Long pagamentoId)
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            if(!validarId(pagamentoId))
            {
                return null;
            }

            ComprovanteDAO comprovanteDAO = new ComprovanteDAO(connection);
            return comprovanteDAO.buscarProPagamentoId(pagamentoId).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<Comprovante> listarTodos()
    {
        try(Connection connection = ConnectionFactory.getConnection())
        {
            ComprovanteDAO comprovanteDAO = new ComprovanteDAO(connection);
            return comprovanteDAO.listarTodos();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    private ComprovanteResponseDTO criarrespostaErro(String mensagem)
    {
        ComprovanteResponseDTO response = new ComprovanteResponseDTO();
        response.setSucesso(false);
        response.setMensagem(mensagem);
        return response;
    }
}
