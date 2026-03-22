package org.frangosInfinity.infrastructure.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class GeradorQRCode
{
    @Autowired
    private QRCodeWriter qrCodeWriter;

    @Value("${qr.code.diretorio:./qrcodes/}")
    private String diretorioBase;

    @Value("${qr.code.tamanho:300}")
    private Integer tamanhoPadrao;

    public String gerarQRCode(String conteudo, String nomeArquivo)
    {
        try
        {
            Path diretorio = Paths.get(diretorioBase);
            Files.createDirectories(diretorio);

            Path caminhoArquivo = diretorio.resolve(nomeArquivo);

            BitMatrix bitMatrix = qrCodeWriter.encode(conteudo, BarcodeFormat.QR_CODE, tamanhoPadrao, tamanhoPadrao);

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

            BitMatrix bitMatrix = qrCodeWriter.encode(
                    conteudo,
                    BarcodeFormat.QR_CODE,
                    tamanhoPadrao,
                    tamanhoPadrao
            );
            return new byte[0];
        }
        catch (WriterException e)
        {
            return null;
        }
    }
}
