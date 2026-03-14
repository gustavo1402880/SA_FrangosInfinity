package org.frangosInfinity.infrastructure.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GeradorQRCode
{
    private final QRCodeWriter qrCodeWriter;
    private final Configuracao configuracao;

    public GeradorQRCode()
    {
        this.qrCodeWriter = new QRCodeWriter();
        this.configuracao = Configuracao.getInstance();
    }

    public String gerarQRCode(String conteudo, String nomeArquivo)
    {
        try
        {
            String diretorBase = configuracao.getProperty("qr.code.diretorio", "./qrcodes/");

            int tamanho = configuracao.getIntProperty("qr.code.tamanho", 300);

            Path diretorio = Paths.get(diretorBase);
            Files.createDirectories(diretorio);

            Path caminhoArquivo = diretorio.resolve(nomeArquivo);

            BitMatrix bitMatrix = qrCodeWriter.encode(conteudo, BarcodeFormat.QR_CODE, tamanho, tamanho);

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", caminhoArquivo);

            return caminhoArquivo.toAbsolutePath().toString();
        }
        catch (WriterException | IOException e)
        {
            return null;
        }
    }

    public String gerarQRCode(String conteudo, String nomeArquivo, int largura, int altura)
    {
        try
        {
            String diretorioBase = configuracao.getProperty("qr.code.diretorio", "./qrcodes/");

            Path diretorio = Paths.get(diretorioBase);
            Files.createDirectories(diretorio);

            Path caminhoArquivo = diretorio.resolve(nomeArquivo);

            BitMatrix bitMatrix = qrCodeWriter.encode(conteudo, BarcodeFormat.QR_CODE, largura, altura);
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", caminhoArquivo);

            return caminhoArquivo.toAbsolutePath().toString();
        }
        catch (WriterException | IOException e)
        {
            return null;
        }
    }

    public byte[] gerarQRCodeBytes(String conteudo)
    {
        try
        {
            int tamanho = configuracao.getIntProperty("qr.code.tamanho", 300);

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    conteudo,
                    BarcodeFormat.QR_CODE,
                    tamanho,
                    tamanho
            );
            return new byte[0];
        }
        catch (WriterException e)
        {
            return null;
        }
    }
}
