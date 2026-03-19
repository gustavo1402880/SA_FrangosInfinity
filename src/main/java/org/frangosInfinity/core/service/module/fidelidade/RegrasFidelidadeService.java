package org.frangosInfinity.core.service.module.fidelidade;

import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.RegrasFidelidadeRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RegrasFidelidadeService
{
    public RegrasFidelidade criarRegras(RegrasFidelidade regras)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);

            if (regras.isAtivo())
            {
                RegrasFidelidade ativa = regrasDAO.buscarAtiva();
                if (ativa != null)
                {
                    ativa.setAtivo(false);
                    regrasDAO.atualizar(ativa);
                }
            }

            boolean salvo = regrasDAO.salvar(regras);

            if (salvo)
            {
                conn.commit();
                return regras;
            }
            else
            {
                return null;
            }

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
            System.err.println("Erro ao criar regras: " + e.getMessage());
            return null;
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

    public RegrasFidelidade buscarRegrasAtivas()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);

            RegrasFidelidade regras = regrasDAO.buscarAtiva();

            if (regras == null)
            {
                regras = new RegrasFidelidade();
                regrasDAO.salvar(regras);
            }

            return regras;

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao buscar regras ativas: " + e.getMessage());
            return new RegrasFidelidade();
        }
    }

    public RegrasFidelidade buscarPorId(Long id)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            if (!validarId(id))
            {
                System.err.println("ID inválido");
                return null;
            }

            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);
            return regrasDAO.buscarPorId(id);

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao buscar regras: " + e.getMessage());
            return null;
        }
    }

    public List<RegrasFidelidade> listarTodas()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);
            return regrasDAO.listarTodas();

        }
        catch (SQLException e)
        {
            System.err.println("Erro ao listar regras: " + e.getMessage());
            return List.of();
        }
    }

    public boolean atualizarRegras(Long id, RegrasFidelidade regrasAtualizadas)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);

            RegrasFidelidade existente = regrasDAO.buscarPorId(id);

            if (existente == null)
            {
                return false;
            }

            existente.setPontosPorReal(regrasAtualizadas.getPontosPorReal());
            existente.setDiasExpiracao(regrasAtualizadas.getDiasExpiracao());
            existente.setPontosMinimosResgate(regrasAtualizadas.getPontosMinimosResgate());
            existente.setValorDescontoPorBloco(regrasAtualizadas.getValorDescontoPorBloco());
            existente.setPontosPorBloco(regrasAtualizadas.getPontosPorBloco());
            existente.setValorMinimoProdutoDesconto(regrasAtualizadas.getValorMinimoProdutoDesconto());

            if (regrasAtualizadas.isAtivo() && !existente.isAtivo())
            {
                RegrasFidelidade ativa = regrasDAO.buscarAtiva();
                if (ativa != null && !ativa.getId().equals(id))
                {
                    ativa.setAtivo(false);
                    regrasDAO.atualizar(ativa);
                }
                existente.setAtivo(true);
            }
            else
            {
                existente.setAtivo(regrasAtualizadas.isAtivo());
            }

            boolean atualizado = regrasDAO.atualizar(existente);

            if (atualizado)
            {
                conn.commit();
            }

            return atualizado;

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
            System.err.println("Erro ao atualizar regras: " + e.getMessage());
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

    public boolean setAtivo(Long id, boolean ativo)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);

            RegrasFidelidade regras = regrasDAO.buscarPorId(id);

            if (regras == null)
            {
                return false;
            }

            if (ativo && !regras.isAtivo())
            {
                RegrasFidelidade ativa = regrasDAO.buscarAtiva();
                if (ativa != null && !ativa.getId().equals(id))
                {
                    ativa.setAtivo(false);
                    regrasDAO.atualizar(ativa);
                }
            }

            regras.setAtivo(ativo);
            boolean atualizado = regrasDAO.atualizar(regras);

            if (atualizado)
            {
                conn.commit();
            }

            return atualizado;

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
            System.err.println("Erro ao alterar status: " + e.getMessage());
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

    public boolean deletar(Long id)
    {
        Connection conn = null;
        try
        {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false);

            RegrasFidelidadeRepository regrasDAO = new RegrasFidelidadeRepository(conn);

            RegrasFidelidade regras = regrasDAO.buscarPorId(id);

            if (regras == null)
            {
                return false;
            }

            if (regras.isAtivo())
            {
                System.err.println("Não é possível deletar regras ativas");
                return false;
            }

            boolean deletado = regrasDAO.deletar(id);

            if (deletado)
            {
                conn.commit();
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
            System.err.println("Erro ao deletar regras: " + e.getMessage());
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

    private boolean validarId(Long id)
    {
        return id != null && id > 0;
    }

    public String getRegrasFormatadas()
    {
        RegrasFidelidade regras = buscarRegrasAtivas();

        return regras.toString();
    }

    public void exibirRegras()
    {
        System.out.println(getRegrasFormatadas());
    }
}
