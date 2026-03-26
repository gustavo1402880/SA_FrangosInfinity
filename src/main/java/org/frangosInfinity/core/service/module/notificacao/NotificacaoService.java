package org.frangosInfinity.core.service.module.notificacao;

import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.frangosInfinity.infrastructure.persistence.module.notificacao.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificacaoService
{
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    @CacheEvict(value = "notificacoes")
    public Notificacao criarNotificacao(TipoNotificacao tipo, String mensagem, String destinatario)
    {
        Notificacao notificacao = new Notificacao(tipo, mensagem, destinatario);

        Notificacao notSalva = notificacaoRepository.save(notificacao);

        if (destinatario != null && destinatario.contains("@") &&
                (tipo == TipoNotificacao.INFO_PEDIDO ||
                 tipo == TipoNotificacao.PEDIDO_CONFIRMADO ||
                 tipo == TipoNotificacao.PEDIDO_PRONTO))
        {
            emailService.enviarEmail(destinatario, tipo.name(), mensagem);
            notSalva.marcarEmailEnviado();
            notificacaoRepository.save(notSalva);
        }

        return notSalva;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "notificacoes", key = "#destinatario")
    public List<Notificacao> buscarPorDestinatario(String destinatario)
    {
        return notificacaoRepository.findByDestinatarioOrderByDataHoraDesc(destinatario);
    }

    @Transactional(readOnly = true)
    public List<Notificacao> buscarNaoLidas(String destinatario)
    {
        return notificacaoRepository.findByLidaFalseAndDestinatarioOrderByDataHoraDesc(destinatario);
    }

    @Transactional(readOnly = true)
    public Long contarNaoLidas(String destinatario)
    {
        return notificacaoRepository.countNaoLidasPorDestinatario(destinatario);
    }

    @Transactional
    @CacheEvict(value = "notificacoes")
    public void marcarComoLida(Long id)
    {
        notificacaoRepository.marcarComoLida(id);
    }

    @Transactional
    @CacheEvict(value = "notificacoes")
    public void  marcarTodasComoLidas(String destinatario)
    {
        notificacaoRepository.marcarTodasComoLidas(destinatario);
    }

    public void notificarBoasVindas(String email, String nome)
    {
        emailService.enviarEmailBoasVindas(email, nome);

        criarNotificacao(TipoNotificacao.INFO_PEDIDO, "Bem-vindo ao Frango's Infinity, " + nome + "!", email);
    }

    public void notificarPedidoConfirmado(String email, String numeroPedido, Double valor)
    {
        criarNotificacao(TipoNotificacao.PEDIDO_CONFIRMADO, "Pedido #" + numeroPedido + " confirmado! Valor: R$ " + String.format("%.2f", valor), email);
    }

    public void notificarPedidoPronto(String email, String numeroPedido)
    {
        criarNotificacao(TipoNotificacao.PEDIDO_CONFIRMADO, "Pedido #" + numeroPedido + " pronto !", email);
    }

    public void notificarPedidoPreparando(String email, String numeroPedido)
    {
        emailService.enviarEmailPedidoPreparando(email, numeroPedido);
    }

    public void notificarPagamentoConfirmado(String email, String numeroPedido)
    {
        emailService.enviarEmailPagamentoConfirmado(email, numeroPedido);

        criarNotificacao(TipoNotificacao.PAGAMENTO_CONFIRMADO, "Pagamento do pedido #" + numeroPedido + " confirmado!", email);
    }

    public void alertarDemora(String destinatario, Long pedidoId)
    {
        String mensagem = String.format("Pedido #%d está atrasado! Verifique a cozinha. ", pedidoId);

        criarNotificacao(TipoNotificacao.ALERTA_DEMORA, mensagem, destinatario);
    }

    public void alertarEstoqueBaixo(String destinatario, String produtoNome, Integer quantidade)
    {
        String mensagem = String.format("Estoque baixo: %s - Disponível: %d unidades", produtoNome, quantidade);

        criarNotificacao(TipoNotificacao.ESTOQUE_BAIXO, mensagem, destinatario);
    }
}
