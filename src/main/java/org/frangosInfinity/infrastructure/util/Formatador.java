package org.frangosInfinity.infrastructure.util;

import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class Formatador
{
    private final DateTimeFormatter formatoDataHora = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private final DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter formatoHora = DateTimeFormatter.ofPattern("HH:mm:ss");

    public String formatarListaMesas(List<Mesa> mesas)
    {
        if (mesas == null || mesas.isEmpty())
        {
            return "Nenhuma mesa cadastrada.";
        }

        StringBuilder sb = new StringBuilder();

        sb.append("\n").append("=".repeat(80)).append("\n");
        sb.append(String.format("| %-4s | %-8s | %-10s | %-25s | %-8s | %-6s |\n",
                "ID", "Número", "Capacidade", "Localização", "Status", "IoT"));
        sb.append("=".repeat(80)).append("\n");

        for (Mesa mesa : mesas)
        {
            String status = mesa.isDisponivel() ? "LIVRE" : "OCUPADA";
            String iotStatus = mesa.getIotConfig().getId() != null ? "CONFIG" : "SEM IoT";

            sb.append(String.format("| %-4d | %-8d | %-10d | %-25s | %-8s | %-6s |\n",
                    mesa.getId(),
                    mesa.getNumero(),
                    mesa.getCapacidade(),
                    truncar(mesa.getLocalizacao(), 25),
                    status,
                    iotStatus
            ));
        }

        sb.append("=".repeat(80));
        return sb.toString();
    }

    public String formatarQRCode(QRCode qrCode)
    {
        if (qrCode == null) {
            return "QR Code não disponível";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\nQR CODE DETALHES:\n");
        sb.append("-".repeat(50)).append("\n");
        sb.append(String.format("ID: %d\n", qrCode.getId()));
        sb.append(String.format("Código: %s\n", qrCode.getCodigo()));
        sb.append(String.format("Mesa: %d\n", qrCode.getIdMesa()));
        sb.append(String.format("Criado: %s\n", formatarDataHora(qrCode.getDataCriacao())));
        sb.append(String.format("Expira: %s\n", formatarDataHora(qrCode.getDataExpiracao())));
        sb.append(String.format("Status: %s\n",
                qrCode.isUtilizado() ? "UTILIZADO" :
                        qrCode.isExpirado() ? "EXPIRADO" :
                                qrCode.isAtivo() ? "ATIVO" : "INATIVO"));
        sb.append(String.format("URL: %s\n", qrCode.getUrlAutenticacao()));
        sb.append("-".repeat(50));

        return sb.toString();
    }

    public String formatarDataHora(LocalDateTime dataHora)
    {
        return dataHora != null ? dataHora.format(formatoDataHora) : "N/A";
    }

    public String formatarData(LocalDateTime data)
    {
        return data != null ? data.format(formatoData) : "N/A";
    }

    public String formatarHora(LocalDateTime hora)
    {
        return hora != null ? hora.format(formatoHora) : "N/A";
    }

    public String formatarTempoRestante(LocalDateTime expiracao)
    {
        if (expiracao == null) return "N/A";

        LocalDateTime agora = LocalDateTime.now();
        if (agora.isAfter(expiracao)) return "EXPIRADO";

        java.time.Duration duracao = java.time.Duration.between(agora, expiracao);
        long segundos = duracao.getSeconds();

        if (segundos < 60)
        {
            return segundos + " segundos";
        }

        long minutos = segundos / 60;
        long segs = segundos % 60;
        return String.format("%d min %d s", minutos, segs);
    }

    public String centralizar(String texto, int largura)
    {
        if (texto == null) texto = "";
        int espacos = (largura - texto.length()) / 2;
        return " ".repeat(Math.max(0, espacos)) + texto;
    }

    private String truncar(String texto, int tamanho)
    {
        if (texto == null) return "";
        if (texto.length() <= tamanho) return texto;
        return texto.substring(0, tamanho - 3) + "...";
    }

    public String formatarMoeda(double valor)
    {
        return String.format("R$ %.2f", valor);
    }
}
