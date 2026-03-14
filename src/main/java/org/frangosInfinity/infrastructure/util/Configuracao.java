package org.frangosInfinity.infrastructure.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuracao
{
    private static Configuracao instancia;
    private final Properties properties;
    private static final String ARQUIVO_CONFIG = "application.properties";

    private Configuracao()
    {
        this.properties = new Properties();
        carregarConfiguracoes();
    }

    public static synchronized Configuracao getInstance()
    {
        if(instancia == null)
        {
            instancia = new Configuracao();
        }
        return instancia;
    }

    private void carregarConfiguracoes()
    {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(ARQUIVO_CONFIG))
        {
            if (input != null)
            {
                properties.load(input);
            }
            else
            {
                carregarValoresPadrao();
            }
        }
        catch (IOException e)
        {
            carregarValoresPadrao();
        }
    }

    private void carregarValoresPadrao()
    {
        properties.setProperty("app.name", "Frangos Infinity");
        properties.setProperty("app.version", "1.0.0");
        properties.setProperty("app.baseUrl", "http://localhost:8080");

        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/frangos_infinity");
        properties.setProperty("db.user", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");

        properties.setProperty("iot.ip.base", "192.168.1");
        properties.setProperty("iot.porta.base", "9000");
        properties.setProperty("iot.tempo.timeout", "5000");

        properties.setProperty("qr.code.diretorio", "./qrcodes/");
        properties.setProperty("qr.code.tamanho", "300");
        properties.setProperty("qr.code.tempo.expiracao", "120");
    }

    public String getProperty(String key, String defaultValue)
    {
        return properties.getProperty(key, defaultValue);
    }

    public int getIntProperty(String key, int defaultValue)
    {
        try
        {
            String value = properties.getProperty(key);

            return value != null ? Integer.parseInt(value) : defaultValue;

        }
        catch (NumberFormatException e)
        {
            return defaultValue;
        }
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        return value != null ? Boolean.parseBoolean(value) : defaultValue;
    }

    public void exibirTodasConfiguracoes()
    {
        System.out.println("\n📋 CONFIGURAÇÕES ATUAIS:");
        System.out.println("=".repeat(40));
        properties.forEach((key, value) ->
                System.out.printf("%-30s: %s%n", key, value)
        );
        System.out.println("=".repeat(40));
    }
}
