package org.frangosInfinity.infrastructure.console.module.fidelidade;

import org.frangosInfinity.application.module.fidelidade.request.ResgateRequestDTO;
import org.frangosInfinity.application.module.fidelidade.response.PontosResponseDTO;
import org.frangosInfinity.application.module.fidelidade.response.RegrasResponseDTO;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.core.service.module.fidelidade.PontosFidelidadeService;
import org.frangosInfinity.core.service.module.fidelidade.RegrasFidelidadeService;

import java.util.ArrayList;
import java.util.List;

public class FidelidadeController
{
    private final PontosFidelidadeService pontosFidelidadeService;
    private final RegrasFidelidadeService regrasFidelidadeService;

    public FidelidadeController()
    {
        this.pontosFidelidadeService = new PontosFidelidadeService();
        this.regrasFidelidadeService = new RegrasFidelidadeService();
    }

    public PontosResponseDTO processarCriarConta(Long clienteId)
    {
        if (clienteId == null || clienteId <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        PontosResponseDTO response = pontosFidelidadeService.criarConta(clienteId);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao criar conta: " + response.getMensagem());
        }

        return response;
    }

    public PontosResponseDTO processarBuscarPontosPorCliente(Long clienteId)
    {
        if (clienteId == null || clienteId <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        PontosResponseDTO response = pontosFidelidadeService.buscarPorCliente(clienteId);

        return response;
    }

    public PontosResponseDTO processarAcumularPontos(Long clienteId, Double valorGasto)
    {
        if (clienteId == null || clienteId <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (valorGasto == null || valorGasto <= 0)
        {
            throw new IllegalArgumentException("Valor gasto deve ser positivo");
        }

        PontosResponseDTO response = pontosFidelidadeService.acumularPontos(clienteId, valorGasto);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao acumular pontos: " + response.getMensagem());
        }

        return response;
    }

    public PontosResponseDTO processarResgatarPontos(ResgateRequestDTO request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("Request não pode ser nulo");
        }

        if (request.getClienteId() == null || request.getClienteId() <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (request.getPontos() == null || request.getPontos() <= 0)
        {
            throw new IllegalArgumentException("Quantidade de pontos inválida");
        }

        if (request.getValorCompra() == null || request.getValorCompra() <= 0)
        {
            throw new IllegalArgumentException("Valor da compra inválido");
        }

        PontosResponseDTO response = pontosFidelidadeService.resgatarPontos(
                request.getClienteId(),
                request.getPontos(),
                request.getValorCompra()
        );

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao resgatar pontos: " + response.getMensagem());
        }

        return response;
    }

    public RegrasResponseDTO processarVerRegras()
    {
        RegrasFidelidade regras = regrasFidelidadeService.buscarRegrasAtivas();

        return RegrasResponseDTO.fromEntity(regras);
    }

    public List<PontosResponseDTO> processarListarTodosClientes()
    {
        List<PontosResponseDTO> lista = pontosFidelidadeService.listarTodos();

        if (lista.isEmpty())
        {
            throw new RuntimeException("Nenhum cliente com pontos cadastrados");
        }

        return lista;
    }

    public PontosResponseDTO processarBuscarPontosPorClienteAdmin(Long clienteId)
    {
        if (clienteId == null || clienteId <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        PontosResponseDTO response = pontosFidelidadeService.buscarPorCliente(clienteId);

        if (!response.getSucesso())
        {
            throw new RuntimeException(response.getMensagem());
        }

        return response;
    }

    public PontosResponseDTO processarAdicionarPontosManual(Long clienteId, Double valorGasto)
    {
        if (clienteId == null || clienteId <= 0)
        {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        if (valorGasto == null || valorGasto <= 0)
        {
            throw new IllegalArgumentException("Valor gasto deve ser positivo");
        }

        PontosResponseDTO response = pontosFidelidadeService.acumularPontos(clienteId, valorGasto);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao adicionar pontos: " + response.getMensagem());
        }

        return response;
    }

    public boolean processarRemoverConta(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        PontosResponseDTO busca = pontosFidelidadeService.buscarPorCliente(id);

        if (!busca.getSucesso())
        {
            throw new RuntimeException("Conta não encontrada");
        }

        boolean removido = pontosFidelidadeService.deletarConta(id);

        if (!removido)
        {
            throw new RuntimeException("Erro ao remover conta de fidelidade");
        }

        return true;
    }

    public RegrasResponseDTO processarCriarRegras(RegrasFidelidade regras)
    {
        if (regras == null)
        {
            throw new IllegalArgumentException("Regras não podem ser nulas");
        }

        if (regras.getPontosPorReal() <= 0)
        {
            throw new IllegalArgumentException("Pontos por real deve ser positivo");
        }

        if (regras.getDiasExpiracao() <= 0)
        {
            throw new IllegalArgumentException("Dias para expiração deve ser positivo");
        }

        if (regras.getPontosMinimosResgate() <= 0)
        {
            throw new IllegalArgumentException("Pontos mínimos para resgate deve ser positivo");
        }

        if (regras.getValorDescontoPorBloco() <= 0)
        {
            throw new IllegalArgumentException("Valor do desconto deve ser positivo");
        }

        if (regras.getPontosPorBloco() <= 0)
        {
            throw new IllegalArgumentException("Pontos por bloco deve ser positivo");
        }

        RegrasFidelidade criadas = regrasFidelidadeService.criarRegras(regras);

        if (criadas == null)
        {
            throw new RuntimeException("Erro ao criar regras de fidelidade");
        }

        return RegrasResponseDTO.fromEntity(regras);
    }

    public RegrasResponseDTO processarAtualizarRegras(Long id, RegrasFidelidade regrasAtualizadas)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        if (regrasAtualizadas == null)
        {
            throw new IllegalArgumentException("Regras não podem ser nulas");
        }

        boolean atualizado = regrasFidelidadeService.atualizarRegras(id, regrasAtualizadas);

        if (!atualizado)
        {
            throw new RuntimeException("Erro ao atualizar regras");
        }

        RegrasFidelidade regras = regrasFidelidadeService.buscarPorId(id);

        return RegrasResponseDTO.fromEntity(regras);
    }

    public boolean processarAtivarRegras(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        boolean ativado = regrasFidelidadeService.setAtivo(id, true);

        if (!ativado)
        {
            throw new RuntimeException("Erro ao ativar regras");
        }

        return true;
    }

    public boolean processarDesativarRegras(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        boolean desativado = regrasFidelidadeService.setAtivo(id, false);

        if (!desativado)
        {
            throw new RuntimeException("Erro ao desativar regras");
        }

        return true;
    }

    public boolean processarDeletarRegras(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID inválido");
        }

        boolean deletado = regrasFidelidadeService.deletar(id);

        if (!deletado)
        {
            throw new RuntimeException("Erro ao deletar regras");
        }

        return true;
    }

    public List<RegrasResponseDTO> processarListarTodasRegras()
    {
        List<RegrasFidelidade> regrasList = regrasFidelidadeService.listarTodas();
        List<RegrasResponseDTO> responseList = new ArrayList<>();

        if (regrasList.isEmpty())
        {
            throw new RuntimeException("Nenhuma regra cadastrada");
        }

        for (RegrasFidelidade regras : regrasList)
        {
            RegrasResponseDTO response = RegrasResponseDTO.fromEntity(regras);

            responseList.add(response);
        }

        return responseList;
    }

    public Integer processarExecutarExpiracaoPontos()
    {
        int totalExpirados = pontosFidelidadeService.processarExpiracaoPontos();
        return totalExpirados;
    }
}
