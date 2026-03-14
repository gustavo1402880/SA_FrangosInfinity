package org.frangosInfinity.core.service.module.notificacao;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService
{
    public static void enviarEmail(String destinatario, String assunto, String mensagem)
    {
        Properties properties = new Properties();

        final String usuario = properties.getProperty("email.user");
        final String senha = properties.getProperty("email.password");

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

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
            message.setText(mensagem);

            Transport.send(message);

            System.out.println("Email enviado!");
        }
        catch(MessagingException e)
        {
            e.printStackTrace();
        }

    }
}
