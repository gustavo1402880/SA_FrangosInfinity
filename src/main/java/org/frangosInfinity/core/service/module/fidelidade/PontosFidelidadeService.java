package org.frangosInfinity.core.service.module.fidelidade;

import org.frangosInfinity.application.module.fidelidade.response.PontosResponseDTO;
import org.frangosInfinity.application.module.fidelidade.response.TransacaoResponseDTO;
import org.frangosInfinity.core.entity.exception.BusinessException;
import org.frangosInfinity.core.entity.exception.ResourceNotFoundException;
import org.frangosInfinity.core.entity.module.fidelidade.PontosFidelidade;
import org.frangosInfinity.core.entity.module.fidelidade.RegrasFidelidade;
import org.frangosInfinity.core.entity.module.fidelidade.TransacaoPontos;
import org.frangosInfinity.core.enums.TipoTransacaoPontos;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.PontosFidelidadeRepository;
import org.frangosInfinity.infrastructure.persistence.module.fidelidade.TransacaoPontosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PontosFidelidadeService
{
    @Autowired
    private PontosFidelidadeRepository pontosFidelidadeRepository;

    @Autowired
    private TransacaoPontosRepository transacaoPontosRepository;

    @Autowired
    private RegrasFidelidadeService regrasService;

    @Transactional
    @CacheEvict(value = "fidelidade", allEntries = true)
    public PontosResponseDTO criarConta(Long clienteId)
    {
        if (!validarId(clienteId))
        {
            return criarRespostaErro("ID do cliente inválido");
        }

        pontosFidelidadeRepository.findByClienteId(clienteId).ifPresent(p -> {throw new BusinessException("Cliente ja possui conta fidelidade");
        });

        PontosFidelidade pontos = new PontosFidelidade(clienteId);
        pontosFidelidadeRepository.save(pontos);

        return converterParaResponseDTO(pontos, "Conta criada com sucesso");
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "fidelidade", key = "#clienteId")
    public PontosResponseDTO buscarPorCliente(Long clienteId)
    {
        if (!validarId(clienteId))
        {
            return criarRespostaErro("ID do cliente inválido");
        }

        PontosFidelidade pontos = pontosFidelidadeRepository.findByClienteId(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não possui programa de fidelidade"));

        List<TransacaoPontos> historico = transacaoPontosRepository.buscarPorPontosId(pontos.getId());
        pontos.setHistorico(historico);

        return converterParaResponseDTO(pontos, "Busca realizada com sucesso");
    }

    @Transactional
    @CacheEvict(value = "fidelidade", key = "#clienteId")
    public PontosResponseDTO acumularPontos(Long clienteId, Double valorGasto)
    {
        if (!validarId(clienteId))
        {
            return criarRespostaErro("ID do cliente inválido");
        }

        if (validarValorMonetario(valorGasto)) {
            return criarRespostaErro("Valor gasto inválido");
        }

        PontosFidelidade pontos = pontosFidelidadeRepository.findByClienteId(clienteId).orElseGet(() -> {
           PontosFidelidade nova = new PontosFidelidade(clienteId);
           return pontosFidelidadeRepository.save(nova);
        });

        RegrasFidelidade regras = regrasService.buscarRegrasAtivas();

        int pontosGanhos = regras.calcularPontosPorValor(valorGasto);

        if (pontosGanhos > 0)
        {
            pontos.adicionarPontos(pontosGanhos);
            pontosFidelidadeRepository.save(pontos);
        }

        List<TransacaoPontos> historico = transacaoPontosRepository.buscarPorPontosId(pontos.getId());
        pontos.setHistorico(historico);

        String mensagem = pontosGanhos > 0 ? pontosGanhos + " pontos acumulados" : "Nenhum ponto acumulado (valor mínimo não atingido)";

        return converterParaResponseDTO(pontos, mensagem);
    }

    @Transactional
    @CacheEvict(value = "fidelidade", key = "#clienteId")
    public PontosResponseDTO resgatarPontos(Long clienteId, Integer pontosResgate, Double valorCompra) {
        if (!validarId(clienteId)) {
            return criarRespostaErro("ID do cliente inválido");
        }

        if (!validarQuantidade(pontosResgate)) {
            return criarRespostaErro("Quantidade de pontos inválida");
        }

        if (!validarValorMonetario(valorCompra)) {
            return criarRespostaErro("Valor da compra inválido");
        }

        PontosFidelidade pontos = pontosFidelidadeRepository.findByClienteId(clienteId).orElseThrow(() -> new ResourceNotFoundException("Cliente não possui pontos"));

        RegrasFidelidade regras = regrasService.buscarRegrasAtivas();

        if (!regras.podeResgatar(pontosResgate, valorCompra))
        {
            return criarRespostaErro(
                    "Mínimo de " + regras.getPontosMinimosResgate() +
                            " pontos e valor mínimo de R$" + regras.getValorMinimoProdutoDesconto()
            );
        }

        if (!pontos.verificarPontosSuficiente(pontosResgate)) {
            return criarRespostaErro("Saldo insuficiente. Saldo atual: " + pontos.getSaldo());
        }

        Double desconto = pontos.calcularDesconto(pontosResgate, valorCompra);

        Boolean resgatado = pontos.resgatarPontos(pontosResgate);

        if (resgatado)
        {
            pontosFidelidadeRepository.save(pontos);

            List<TransacaoPontos> historico = transacaoPontosRepository.buscarPorPontosId(pontos.getId());
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

    @Transactional
    @CacheEvict(value = "fidelidade", allEntries = true)
    public int processarExpiracaoPontos()
    {
        List<PontosFidelidade> expirados = pontosFidelidadeRepository.buscarExpirados();
        int totalExpirados = 0;

        for (PontosFidelidade pontos : expirados)
        {
            Integer saldo = pontos.getSaldo();
            if (saldo > 0)
            {
                pontos.expirarPontos(saldo);
                pontosFidelidadeRepository.save(pontos);
                totalExpirados += saldo;
            }
        }
        return totalExpirados;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "fidelidade", key = "'todos'")
    public List<PontosResponseDTO> listarTodos()
    {
        return pontosFidelidadeRepository.findAll().stream()
                .map(p ->
                {
                    List<TransacaoPontos> historico = transacaoPontosRepository.buscarPorPontosId(p.getId());
                    p.setHistorico(historico);
                    return converterParaResponseDTO(p, null);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "fidelidade", key = "#pontos.clienteId")
    public boolean deletarConta(Long id)
    {
        PontosFidelidade pontos = pontosFidelidadeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Conta de fidelidade não encontrada"));

        Long clienteId = pontos.getClienteId();
        pontosFidelidadeRepository.delete(pontos);

        return true;
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
            List<TransacaoResponseDTO> historicoDTO =
                    pontos.getHistorico().stream()
                            .map(this::converterTransacaoParaDTO)
                            .collect(Collectors.toList());
            dto.setHistorico(historicoDTO);
        }

        return dto;
    }

    private TransacaoResponseDTO converterTransacaoParaDTO(TransacaoPontos transacao)
    {
        TransacaoResponseDTO dto = new TransacaoResponseDTO();
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
