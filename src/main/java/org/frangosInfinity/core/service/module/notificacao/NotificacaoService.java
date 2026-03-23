package org.frangosInfinity.core.service.module.notificacao;

import org.frangosInfinity.core.entity.module.notificacao.Notificacao;
import org.frangosInfinity.core.enums.TipoNotificacao;
import org.frangosInfinity.infrastructure.persistence.module.notificacao.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (destinatario != null && destinatario.contains("@") && (tipo == TipoNotificacao.INFO_PEDIDO))
    }


    public static void notificacaoEmail()
    {
        String destinatario = "email_destinatario@estudante.sesisenai.org.br";
        String assunto = "Frango's Fritos - Cadastro";
        String mensagem = """
                        <html>
                        <body style="margin:0; padding:0; background-color:#f2f2f2; font-family:Arial, Helvetica, sans-serif;">
                        
                        <table width="100%" cellpadding="0" cellspacing="0" border="0">
                        <tr>
                        <td align="center" style="padding:40px 0;">
                        
                        <table width="500" cellpadding="0" cellspacing="0" border="0" 
                        style="background-color:#ffffff; padding:30px; border-radius:8px; box-shadow:0 2px 8px rgba(0,0,0,0.1);">
                        
                        <tr>
                        <td>
                        
                        <h2 style="color:green; margin-top:0;">Implementação concluída ✅</h2>
                        
                        <p>Olá,</p>
                        
                        <p>
                        A implementação do sistema de <b>notificações por e-mail</b> foi realizada com sucesso.
                        </p>
                        
                        <p>
                        Este e-mail foi enviado utilizando um <b>endereço alternativo configurado no sistema</b>.
                        </p>
                        
                        <p style="color:blue;">
                        Caso você tenha recebido esta mensagem, significa que o envio está funcionando corretamente.
                        </p>
                        
                        <br>
                        
                        <p>
                        Atenciosamente,<br>
                        <b>Equipe de Desenvolvimento</b><br>
                        Frangos Infinity
                        </p>
                        
                        </td>
                        </tr>
                        
                        </table>
                        
                        </td>
                        </tr>
                        </table>
                        
                        </body>
                        </html>
                        """;

        EmailService.enviarEmail(destinatario, assunto, mensagem);
    }
}
