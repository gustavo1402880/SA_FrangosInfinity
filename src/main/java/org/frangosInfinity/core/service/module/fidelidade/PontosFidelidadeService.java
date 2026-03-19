package org.frangosInfinity.core.service.module.fidelidade;

import org.frangosInfinity.application.module.fidelidade.response.PontosResponseDTO;
import org.frangosInfinity.application.module.fidelidade.response.TransacaoResponseDTO;
import org.frangosInfinity.core.entity.module.fidelidade.PontosFidelidade;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.core.entity.module.fidelidade.TransacaoPontos;
import org.frangosInfinity.core.enums.TipoTransacaoPontos;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.PontosFidelidadeRepository;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.TransacaoPontosRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class PontosFidelidadeService
{
    private final RegrasFidelidadeService regrasService;

    public PontosFidelidadeService()
    {
        this.regrasService = new RegrasFidelidadeService();
    }

    public PontosResponseDTO criarConta(Long clienteId)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {

            if (!validarId(clienteId))
            {
                return criarRespostaErro("ID do cliente inválido");
            }

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);

            PontosFidelidade existente = pontosDAO.buscarPorClienteId(clienteId);
            if (existente != null)
            {
                return converterParaResponseDTO(existente, "Conta já existente");
            }

            PontosFidelidade pontos = new PontosFidelidade(clienteId);
            pontosDAO.salvar(pontos);

            System.out.println("Conta de fidelidade criada para cliente " + clienteId);

            return converterParaResponseDTO(pontos, "Conta criada com sucesso");

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao criar conta de fidelidade: " + e.getMessage());
            return criarRespostaErro("Erro ao criar conta: " + e.getMessage());
        }
    }

    public PontosResponseDTO buscarPorCliente(Long clienteId)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            if (!validarId(clienteId))
            {
                return criarRespostaErro("ID do cliente inválido");
            }

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            PontosFidelidade pontos = pontosDAO.buscarPorClienteId(clienteId);

            if (pontos == null)
            {
                return criarRespostaErro("Cliente não possui programa de fidelidade");
            }

            List<TransacaoPontos> historico = transacaoDAO.buscarPorPontosId(pontos.getId());
            pontos.setHistorico(historico);

            return converterParaResponseDTO(pontos, "Busca realizada com sucesso");

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao buscar pontos: " + e.getMessage());
            return criarRespostaErro("Erro ao buscar pontos: " + e.getMessage());
        }
    }

    public PontosResponseDTO acumularPontos(Long clienteId, Double valorGasto)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            if (!validarId(clienteId))
            {
                return criarRespostaErro("ID do cliente inválido");
            }

            if (validarValorMonetario(valorGasto))
            {
                return criarRespostaErro("Valor gasto inválido");
            }

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            PontosFidelidade pontos = pontosDAO.buscarPorClienteId(clienteId);

            if (pontos == null)
            {
                pontos = new PontosFidelidade(clienteId);
                pontosDAO.salvar(pontos);
                pontos = pontosDAO.buscarPorClienteId(clienteId);
            }

            RegrasFidelidade regras = regrasService.buscarRegrasAtivas();

            int pontosGanhos = regras.calcularPontosPorValor(valorGasto);

            if (pontosGanhos > 0)
            {
                pontos.adicionarPontos(pontosGanhos);

                TransacaoPontos transacao = new TransacaoPontos(pontos.getId(), TipoTransacaoPontos.ACUMULO, pontosGanhos);
                transacaoDAO.salvar(transacao);

                pontosDAO.atualizar(pontos);

                conn.commit();
                System.out.println(pontosGanhos + " pontos acumulados para cliente " + clienteId);
            }

            List<TransacaoPontos> historico = transacaoDAO.buscarPorPontosId(pontos.getId());
            pontos.setHistorico(historico);

            String mensagem = pontosGanhos > 0 ? pontosGanhos + " pontos acumulados" : "Nenhum ponto acumulado (valor mínimo não atingido)";

            return converterParaResponseDTO(pontos, mensagem);

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                    System.err.println("Rollback realizado devido a erro");
                }
                catch (SQLException ex)
                {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao acumular pontos: " + e.getMessage());
            return criarRespostaErro("Erro ao acumular pontos: " + e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public PontosResponseDTO resgatarPontos(Long clienteId, Integer pontosResgate, Double valorCompra)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            if (!validarId(clienteId))
            {
                return criarRespostaErro("ID do cliente inválido");
            }

            if (!validarQuantidade(pontosResgate))
            {
                return criarRespostaErro("Quantidade de pontos inválida");
            }

            if (!validarValorMonetario(valorCompra))
            {
                return criarRespostaErro("Valor da compra inválido");
            }

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            PontosFidelidade pontos = pontosDAO.buscarPorClienteId(clienteId);

            if (pontos == null)
            {
                return criarRespostaErro("Cliente não possui pontos");
            }

            // Busca regras ativas
            RegrasFidelidade regras = regrasService.buscarRegrasAtivas();

            if (!regras.podeResgatar(pontosResgate, valorCompra))
            {
                return criarRespostaErro(
                        "Mínimo de " + regras.getPontosMinimosResgate() +
                                " pontos e valor mínimo de R$" + regras.getValorMinimoProdutoDesconto()
                );
            }

            if (!pontos.verificarPontosSuficiente(pontosResgate))
            {
                return criarRespostaErro("Saldo insuficiente. Saldo atual: " + pontos.getSaldo());
            }

            Double desconto = pontos.calcularDesconto(pontosResgate, valorCompra);

            Boolean resgatado = pontos.resgatarPontos(pontosResgate);

            if (resgatado)
            {
                TransacaoPontos transacao = new TransacaoPontos(pontos.getId(), TipoTransacaoPontos.RESGATE, pontosResgate);
                transacaoDAO.salvar(transacao);

                pontosDAO.atualizar(pontos);

                conn.commit();
                System.out.println(pontosResgate + " pontos resgatados para cliente " + clienteId);

                List<TransacaoPontos> historico = transacaoDAO.buscarPorPontosId(pontos.getId());
                pontos.setHistorico(historico);

                PontosResponseDTO response = converterParaResponseDTO(pontos,
                        "Resgate realizado! Desconto de R$" + String.format("%.2f", desconto));
                return response;
            }
            else
            {
                return criarRespostaErro("Erro ao resgatar pontos");
            }

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                    System.err.println("Rollback realizado devido a erro");
                }
                catch (SQLException ex)
                {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao resgatar pontos: " + e.getMessage());
            return criarRespostaErro("Erro ao resgatar pontos: " + e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    public int processarExpiracaoPontos()
    {
        Connection conn = null;
        int totalExpirados = 0;

        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            List<PontosFidelidade> todos = pontosDAO.listarTodos();
            LocalDateTime hoje = LocalDateTime.now();

            for (PontosFidelidade pontos : todos)
            {
                if (pontos.getDataValidade() != null && pontos.getDataValidade().isBefore(hoje))
                {
                    Integer saldo = pontos.getSaldo();
                    if (saldo > 0)
                    {
                        pontos.expirarPontos(saldo);

                        TransacaoPontos transacao = new TransacaoPontos(pontos.getId(), TipoTransacaoPontos.EXPIRACAO, saldo);
                        transacaoDAO.salvar(transacao);
                        pontosDAO.atualizar(pontos);

                        totalExpirados += saldo;
                        System.out.println(saldo + " pontos expirados do cliente " + pontos.getClienteId());
                    }
                }
            }

            conn.commit();
            System.out.println("Processamento de expiração concluído. Total expirado: " + totalExpirados);

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException ex)
                {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao processar expiração: " + e.getMessage());
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }

        return totalExpirados;
    }

    public List<PontosResponseDTO> listarTodos()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            List<PontosFidelidade> todos = pontosDAO.listarTodos();

            return todos.stream()
                    .map(p ->
                    {
                        List<TransacaoPontos> historico = transacaoDAO.buscarPorPontosId(p.getId());
                        p.setHistorico(historico);
                        return converterParaResponseDTO(p, null);
                    })
                    .collect(Collectors.toList());

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao listar clientes: " + e.getMessage());
            return List.of();
        }
    }

    public boolean deletarConta(Long id)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            PontosFidelidadeRepository pontosDAO = new PontosFidelidadeRepository(conn);
            TransacaoPontosRepository transacaoDAO = new TransacaoPontosRepository(conn);

            List<TransacaoPontos> transacoes = transacaoDAO.buscarPorPontosId(id);
            for (TransacaoPontos t : transacoes)
            {
                transacaoDAO.deletar(t.getId());
            }

            boolean deletado = pontosDAO.deletar(id);

            if (deletado)
            {
                conn.commit();
                System.out.println("Conta de fidelidade " + id + " deletada");
            }

            return deletado;

        }
        catch (SQLException e)
        {
            if (conn != null)
            {
                try
                {
                    conn.rollback();
                }
                catch (SQLException ex)
                {
                    System.err.println("Erro no rollback: " + ex.getMessage());
                }
            }
            System.err.println("Erro ao deletar conta: " + e.getMessage());
            return false;
        }
        finally
        {
            if (conn != null)
            {
                try
                {
                    conn.close();
                }
                catch (SQLException e)
                {
                    System.err.println("Erro ao fechar conexão: " + e.getMessage());
                }
            }
        }
    }

    private PontosResponseDTO criarRespostaErro(String mensagem)
    {
        PontosResponseDTO dto = new PontosResponseDTO();
        dto.setSucesso(false);
        dto.setMensagem(mensagem);
        return dto;
    }

    private PontosResponseDTO converterParaResponseDTO(PontosFidelidade pontos, String mensagem)
    {
        PontosResponseDTO dto = new PontosResponseDTO();
        dto.setId(pontos.getId());
        dto.setClienteId(pontos.getClienteId());
        dto.setSaldo(pontos.getSaldo());
        dto.setSucesso(true);

        if (mensagem != null)
        {
            dto.setMensagem(mensagem);
        }

        if (pontos.getDataValidade() != null)
        {
            dto.setDataValidade(pontos.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (pontos.getHistorico() != null && !pontos.getHistorico().isEmpty())
        {
            List<org.frangosInfinity.application.module.fidelidade.response.TransacaoResponseDTO> historicoDTO =
                    pontos.getHistorico().stream()
                            .map(this::converterTransacaoParaDTO)
                            .collect(Collectors.toList());
            dto.setHistorico(historicoDTO);
        }

        return dto;
    }

    private TransacaoResponseDTO converterTransacaoParaDTO(TransacaoPontos transacao)
    {
        org.frangosInfinity.application.module.fidelidade.response.TransacaoResponseDTO dto =
                new org.frangosInfinity.application.module.fidelidade.response.TransacaoResponseDTO();
        dto.setId(transacao.getId());
        dto.setData(transacao.getData().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
        dto.setTipo(transacao.getTipoTransacaoPontos());
        dto.setQuantidade(transacao.getQuantidade());
        return dto;
    }

    private boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    private boolean validarValorMonetario(Double valor)
    {
        return valor != null && valor > 0;
    }

    private boolean validarQuantidade(Integer quantidade)
    {
        return quantidade != null && quantidade > 0;
    }
}
