package org.frangosInfinity.infrastructure.console.module.pagamento;

import org.frangosInfinity.application.module.pagamento.request.PagamentoRequestDTO;
import org.frangosInfinity.application.module.pagamento.response.ComprovanteResponseDTO;
import org.frangosInfinity.application.module.pagamento.response.PIXResponseDTO;
import org.frangosInfinity.application.module.pagamento.response.PagamentoResponseDTO;
import org.frangosInfinity.core.entity.module.pagamento.Comprovante;
import org.frangosInfinity.core.entity.module.pagamento.Pagamento;
import org.frangosInfinity.core.entity.module.pagamento.TransacaoPIX;
import org.frangosInfinity.core.service.module.pagamento.ComprovanteService;
import org.frangosInfinity.core.service.module.pagamento.PagamentoService;
import org.frangosInfinity.core.service.module.pagamento.TransacaoPIXService;

import java.util.List;
import java.util.stream.Collectors;

public class PagamentoController
{
    private final PagamentoService pagamentoService;
    private final TransacaoPIXService pixService;
    private final ComprovanteService comprovanteService;

    public PagamentoController()
    {
        this.pagamentoService = new PagamentoService();
        this.pixService = new TransacaoPIXService();
        this.comprovanteService = new ComprovanteService();
    }

    public PagamentoResponseDTO processarPagamento(PagamentoRequestDTO request)
    {
        if (request == null)
        {
            throw new IllegalArgumentException("Request de pagamento não pode ser nulo");
        }

        if (request.getSubPedidoId() == null || request.getSubPedidoId() <= 0)
        {
            throw new IllegalArgumentException("ID do subpedido inválido");
        }

        if (request.getValor() == null || request.getValor() <= 0)
        {
            throw new IllegalArgumentException("Valor do pagamento inválido");
        }

        if (request.getTipo() == null)
        {
            throw  new IllegalArgumentException("Tipo de pagamento não especificado");
        }

        PagamentoResponseDTO response = pagamentoService.processarPagamento(request);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao processar pagamento: "+response.getMensagem());
        }

        return response;
    }

    public PagamentoResponseDTO processarBuscarPagamentoPorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoService.buscarPorId(id);

        if (pagamento == null)
        {
            throw new RuntimeException("Pagamento com ID "+id+" não encontrado");
        }

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    public PagamentoResponseDTO processarBuscarPagamentoPorSubPedido(Long subPedidoId)
    {
        if (subPedidoId == null || subPedidoId <= 0)
        {
            throw new IllegalArgumentException("ID do subpedido inválido");
        }

        Pagamento pagamento = pagamentoService.buscarPorsubPedidoId(subPedidoId);

        if (pagamento == null)
        {
            throw new RuntimeException("Pagamento para o subpedido "+subPedidoId+" não encontrado");
        }

        return PagamentoResponseDTO.fromEntity(pagamento);
    }

    public List<PagamentoResponseDTO> processarListarTodosPagamentos()
    {
        List<Pagamento> pagamentos = pagamentoService.listarTodos();

        if (pagamentos.isEmpty())
        {
            throw new RuntimeException("Nenhum pagamento encontrado");
        }

        return pagamentos.stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<PagamentoResponseDTO> processarListarPagamentosPendentes()
    {
        List<Pagamento> pagamentos = pagamentoService.listarPendetes();

        if (pagamentos.isEmpty())
        {
            throw new RuntimeException("Nenhum pagamento pendente encontrado");
        }

        return pagamentos.stream()
                .map(PagamentoResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PagamentoResponseDTO processarConfirmarPagamento(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        PagamentoResponseDTO response = pagamentoService.confirmarPagamento(pagamentoId);


        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao confirmar pagamento: "+ response.getMensagem());
        }

        return response;
    }

    public PagamentoResponseDTO processarCancelarPagamento(Long pagamentoId)
    {
        if(pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        PagamentoResponseDTO response = pagamentoService.cancelarPagamento(pagamentoId);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao cancelar pagamento: "+response.getMensagem());
        }

        return response;
    }

    public PIXResponseDTO processarGerarPix(Long pagamentoId, Integer tempoExpiracaoSegundos)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        PIXResponseDTO response;

        if (tempoExpiracaoSegundos != null && tempoExpiracaoSegundos > 0)
        {
            response = pixService.gerarPix(pagamentoId, tempoExpiracaoSegundos);
        }
        else
        {
            response = pixService.gerarPix(pagamentoId);
        }

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao gerar Pix: "+response.getMensagem());
        }

        return response;
    }

    public PIXResponseDTO processarBuscarPixPorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID do PIX inválido");
        }

        TransacaoPIX pix = pixService.buscarPorId(id);

        if (pix == null)
        {
            throw new RuntimeException("PIX: "+id+" não encontrado");
        }

        return PIXResponseDTO.fromEntity(pix);
    }

    public PIXResponseDTO processarBuscarPixPorPagamento(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        TransacaoPIX pix = pixService.buscarPorPagamentoId(pagamentoId);

        if (pix == null)
        {
            throw new RuntimeException("PIX para o pagamento "+pagamentoId+" não encontrado");
        }

        return PIXResponseDTO.fromEntity(pix);
    }

    public PIXResponseDTO processarRenovarPix(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        PIXResponseDTO response = pixService.renovarPix(pagamentoId);

        if (!response.getSucesso())
        {
            throw new RuntimeException("Erro ao renovar PIX: "+response.getMensagem());
        }

        return response;
    }

    public Boolean processarVerifiacarExpiracaoPix(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        return pixService.verificarExpiracao(pagamentoId);
    }

    public ComprovanteResponseDTO processarBuscarComprovantePorId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("ID do comprovante inválido");
        }

        Comprovante comprovante = comprovanteService.buscarPorId(id);

        if (comprovante == null)
        {
            throw new RuntimeException("Comprovante com ID: "+id+" não encontrado");
        }

        return ComprovanteResponseDTO.fromEntity(comprovante);
    }

    public ComprovanteResponseDTO processarBuscarComprovantePorPagamento(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        Comprovante comprovante = comprovanteService.buscarporPagamentoId(pagamentoId);

        if (comprovante == null)
        {
            throw new RuntimeException("Comprovante para o pagamento "+pagamentoId+" não encontrado");
        }

        return ComprovanteResponseDTO.fromEntity(comprovante);
    }

    public List<ComprovanteResponseDTO> processasrListarTodosComprovantes()
    {
        List<Comprovante> comprovantes = comprovanteService.listarTodos();

        if (comprovantes.isEmpty())
        {
            throw new RuntimeException("Nenhum comprovante encontrado");
        }

        return comprovantes.stream()
                .map(ComprovanteResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public ComprovanteResponseDTO processarGerarComprovanteParaPagamento(Long pagamentoId)
    {
        if (pagamentoId == null || pagamentoId <= 0)
        {
            throw new IllegalArgumentException("ID do pagamento inválido");
        }

        Pagamento pagamento = pagamentoService.buscarPorId(pagamentoId);
        if (pagamento == null)
        {
            throw new RuntimeException("Pagamento não encontrado");
        }

        Comprovante comprovante = comprovanteService.buscarporPagamentoId(pagamentoId);

        if (comprovante == null)
        {
            throw new RuntimeException("Comprovante não encontrado para este pagamento");
        }

        return ComprovanteResponseDTO.fromEntity(comprovante);
    }
}
