package org.frangosInfinity.core.service.module.notificacao;

public class NotificacaoService
{
    public static void notificacaoEmail()
    {
        String destinatario = "email_destinatario@estudante.sesisenai.org.br";
        String assunto = "Frango's Fritos - Cadastro";
        String mensagem = "Olá!\n" +
                "\n" +
                "Seu cadastro foi realizado com sucesso.\n" +
                "\n" +
                "A partir de agora você poderá realizar pedidos e acompanhar o andamento deles diretamente pelo sistema. Sempre que houver uma atualização importante, você receberá uma notificação.\n" +
                "\n" +
                "Agradecemos por se cadastrar!\n" +
                "\n" +
                "Atenciosamente,  \n" +
                "Equipe de Atendimento";

        EmailService.enviarEmail(destinatario, assunto, mensagem);
    }
}
