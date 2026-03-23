package org.frangosInfinity.core.service.module.notificacao;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Properties;

@Service
public class EmailService
{
    @Value("${email.user:}")
    private String usuario;

    @Value("${email.password:}")
    private String senha;

    @Value("${spring.mail.host:stmp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:587}")
    private String porta;

    @Async
    public void enviarEmail(String destinatario, String assunto, String mensagem)
    {
        Properties properties = new Properties();

        final String usuario = properties.getProperty("email.user");
        final String senha = properties.getProperty("email.password");

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", porta);

        Session session = Session.getInstance(properties, new Authenticator()
        {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(usuario, senha);
            }
        });

        try
        {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));

            message.setSubject(assunto);
            message.setContent(mensagem, "text/html; charset=utf-8");

            Transport.send(message);

            System.out.println("Email enviado!");
        }
        catch(MessagingException e)
        {
            e.printStackTrace();
        }

    }

    public void enviarEmailBoasVindas(String destinatario, String nome)
    {
        String assunto = "Bem-vindo ao Frango's Infinity!";
        String message = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif;">
                     <h2 style="color: #ff6b35;">Olá, %s!</h2>
                     <p>Seja bem-vindo ao <strong>Frango's Infinity</strong>!</p>
                     <p>Estamos muito felizes em ter você conosco.</p>
                     <p>Agora você pode fazer seus pedidos de forma rápida e prática.</p>
                     <br>
                     <p>Atenciosamente,<br>Equipe Frango's Infinity</p>
                </body>
                </html>
                """, nome);
        enviarEmail(destinatario, assunto, message);
    }

    public void enviarEmailPedidoConfirmado(String destinatario, String numeroPedido, Double valor)
    {
        String assunto = "Pedido Confirmado! - "+numeroPedido;
        String message = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif;">
                     <h2 style="color: #28a745;">Pedido Confirmado!</h2>
                     <p><strong>Número do pedido:</strong> %s</p>
                     <p><strong>Valor total:</strong> R$ %.2f</p>
                     <p>Seu pedido já está sendo preparado.</p>
                     <p>Acompanhe o status pelo nosso sistema.</p>
                     <br>
                     <p>Atenciosamente,<br>Equipe Frango's Infinity</p>
                </body>
                </html>
                """, numeroPedido, valor);
        enviarEmail(destinatario, assunto, message);
    }

    public void enviarEmailPedidoPronto(String destinatario, String numeroPedido)
    {
        String assunto = "Pedido Pronto! - "+numeroPedido;
        String message = String.format("""
                <html>
                <body style="font-family: Arial, sans-serif;">
                     <h2 style="color: #28a745;">Seu pedido está pronto!</h2>
                     <p><strong>Número do pedido:</strong> %s</p>
                     <p>Seu pedido já está pronto para retirada/entrega.</p>
                     <p>Agradecemos pela preferência!</p>
                     <br>
                     <p>Atenciosamente,<br>Equipe Frango's Infinity</p>
                </body>
                </html>
                """, numeroPedido);
        enviarEmail(destinatario, assunto, message);
    }
}
