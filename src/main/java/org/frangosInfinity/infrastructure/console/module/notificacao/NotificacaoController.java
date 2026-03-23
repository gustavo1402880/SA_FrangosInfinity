package org.frangosInfinity.infrastructure.console.module.notificacao;

import com.sun.mail.iap.Response;
import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.frangosInfinity.core.service.module.notificacao.EmailService;
import org.frangosInfinity.core.service.module.notificacao.NotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController
{
    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/{destinatario}")
    public ResponseEntity<List<Notificacao>> processarBuscarPorDestinatario(@PathVariable String destinatario)
    {
        List<Notificacao> notificacoes = notificacaoService.buscarPorDestinatario(destinatario);

        return notificacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/{destinatario}/nao-lidas")
    public ResponseEntity<List<Notificacao>> processarBuscarNaoLidas(@PathVariable String destinatario)
    {
        List<Notificacao> notificacoes = notificacaoService.buscarNaoLidas(destinatario);

        return notificacoes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(notificacoes);
    }

    @GetMapping("/{destinatario}/count")
    public ResponseEntity<Long> processarContarNaoLidas(@PathVariable String destinatario)
    {
        return ResponseEntity.ok(notificacaoService.contarNaoLidas(destinatario));
    }

    @PatchMapping("/{id}/lida")
    public ResponseEntity<Void> processarMarcarComoLida(@PathVariable Long id)
    {
        notificacaoService.marcarComoLida(id);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{destinatario}/lidas")
    public ResponseEntity<Void> processarMarcarTodasComoLidas(@PathVariable String destinatario)
    {
        notificacaoService.marcarTodasComoLidas(destinatario);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cozinheiro/pedido-novo")
    public ResponseEntity<Notificacao> processarNotificarCozinhaNovoPedido(@RequestParam String numeroPedido, @RequestParam Integer quantidadeItens)
    {
        String mensagem = String.format("NOVO PEDIDO #%s - %d itens para preparar", numeroPedido, quantidadeItens);

        Notificacao notificacao = notificacaoService.criarNotificacao(
                TipoNotificacao.INFO_PEDIDO,
                mensagem,
                "COZINHEIRO"
        );

        return ResponseEntity.ok(notificacao);
    }

    @PostMapping("/cozinheiro/pedido-confirmado")
    public ResponseEntity<Notificacao> processarNotificarCozinhaPedidoConfirmado(@RequestParam String numeroPedido, @RequestParam Integer quantidadeItens)
    {
        String mensagem = String.format("PEDIDO #%s CONFIRMADO! Iniciar preparo.", numeroPedido);

        Notificacao notificacao = notificacaoService.criarNotificacao(
                TipoNotificacao.PEDIDO_CONFIRMADO,
                mensagem,
                "COZINHEIRO"
        );

        return ResponseEntity.ok(notificacao);
    }

    @PostMapping("/atendente/pedido-pronto")
    public ResponseEntity<Notificacao> processarNotificarAtendentePedidoPronto(@RequestParam String numeroPedido)
    {
        String mensagem = String.format("Pedido #%s PRONTO! Entregar ao cliente.", numeroPedido);

        Notificacao notificacao = notificacaoService.criarNotificacao(
                TipoNotificacao.PEDIDO_PRONTO,
                mensagem,
                "ATENDENTE"
        );

        return ResponseEntity.ok(notificacao);
    }

    @PostMapping("/admin/estoque-baixo")
    public ResponseEntity<Notificacao> processarNotificarAdminEstoqueBaixo(@RequestParam String produtoNome, @RequestParam Integer quantidadeAtual)
    {
        notificacaoService.alertarEstoqueBaixo("ADMINISTRADOR",produtoNome, quantidadeAtual);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/pedido-atrasado")
    public ResponseEntity<Void> processarNotificarAdminPedidoAtrasado(@RequestParam Long pedidoId, @RequestParam Integer minutosAtraso)
    {
        String mensagem = String.format("ALERTA: Pedido #%d atrasado há minutos!", pedidoId, minutosAtraso);

        notificacaoService.criarNotificacao(
                TipoNotificacao.ALERTA_DEMORA,
                mensagem,
                "ADMINISTRADOR"
        );

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cliente/pedido-confirmado")
    public ResponseEntity<Void> processarNotificarClientePedidoConfirmado(@RequestParam String email, @RequestParam String numeroPedido, @RequestParam Double valor)
    {
        notificacaoService.notificarPedidoConfirmado(email, numeroPedido, valor);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cliente/pedido-preparando")
    public ResponseEntity<Void> processarNotificarClientePedidoPreparando(@RequestParam String email, @RequestParam String numeroPedido)
    {
        notificacaoService.notificarPedidoPreparando(email, numeroPedido);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cliente/pedido-pronto")
    public ResponseEntity<Void> processarNotificarClientePedidoPronto(@RequestParam String email, @RequestParam String numeroPedido)
    {
        notificacaoService.notificarPedidoPronto(email, numeroPedido);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cliente/pagamento-confirmado")
    public ResponseEntity<Void> processarNotificarClientePagamentoConfirmado(@RequestParam String email, @RequestParam String numeroPedido)
    {
        notificacaoService.notificarPagamentoConfirmado(email, numeroPedido);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cliente/boas-vindas")
    public ResponseEntity<Void> processarNotificarClienteBoasVindas(@RequestParam String email, @RequestParam String nome)
    {
        notificacaoService.notificarBoasVindas(email, nome);

        return ResponseEntity.ok().build();
    }
}
