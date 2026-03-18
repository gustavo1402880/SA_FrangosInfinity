package org.frangosInfinity.infrastructure.util;

import org.frangosInfinity.application.module.mesa.request.QRCodeRequestDTO;
import org.frangosInfinity.core.entity.module.mesa.Mesa;
import org.frangosInfinity.core.entity.module.mesa.QRCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validator
{
    @Value("${mesa.capacidade.maxima:20}")
    private Integer capacidadeMaxima;

    public Boolean validarRequestQRCode(QRCodeRequestDTO requestDTO)
    {
        if(requestDTO == null)
        {
            return false;
        }

        if (requestDTO.getIdMesa() <= 0)
        {
            return false;
        }

        if(requestDTO.getTokenAcesso() == null || requestDTO.getTokenAcesso().trim().isEmpty())
        {
            return false;
        }

        return true;
    }

    public boolean validarQRCode(QRCode qrCode)
    {
        if (qrCode == null)
        {
            return false;
        }

        if (qrCode.getCodigo() == null || qrCode.getCodigo().trim().isEmpty())
        {
            return false;
        }

        if (qrCode.getUrlAutenticacao() == null || qrCode.getUrlAutenticacao().trim().isEmpty())
        {
            return false;
        }

        if (qrCode.getIdMesa().getId() <= 0)
        {
            return false;
        }

        if (qrCode.getDataCriacao() == null || qrCode.getDataExpiracao() == null)
        {
            return false;
        }

        if (qrCode.getDataExpiracao().isBefore(qrCode.getDataCriacao()))
        {
            return false;
        }

        return true;
    }

    public boolean validarMesa(Mesa mesa)
    {
        if (mesa == null)
        {
            return false;
        }

        if (!validarNumeroMesa(mesa.getNumero()))
        {
            return false;
        }

        if (!validarCapacidade(mesa.getCapacidade()))
        {
            return false;
        }

        if (mesa.getLocalizacao() == null || mesa.getLocalizacao().trim().isEmpty())
        {
            return false;
        }

        return true;
    }

    public boolean validarNumeroMesa(int numero)
    {
        if (numero <= 0)
        {
            return false;
        }

        if (numero > 999)
        {
            return false;
        }

        return true;
    }

    public boolean validarCapacidade(int capacidade)
    {
        if (capacidade <= 0)
        {
            return false;
        }

        if (capacidade > capacidadeMaxima)
        {
            return false;
        }

        return true;
    }

    public boolean validarTokenSessao(String token)
    {
        if (token == null || token.trim().isEmpty())
        {
            return false;
        }

        if (token.length() < 8 || token.length() > 36)
        {
            return false;
        }

        return true;
    }

    public boolean validarUrl(String url)
    {
        if (url == null || url.trim().isEmpty())
        {
            return false;
        }

        return url.startsWith("http://") || url.startsWith("https://");
    }

    public boolean validarExpiracao(java.time.LocalDateTime dataExpiracao)
    {
        if (dataExpiracao == null)
        {
            return false;
        }

        return !dataExpiracao.isBefore(java.time.LocalDateTime.now());
    }
}
