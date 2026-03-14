package org.frangosInfinity.infrastructure.persistence.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionFactory
{
    public static Connection getConnection() throws SQLException
    {
        Properties properties = new Properties();

        try (InputStream input = ConnectionFactory.class.getClassLoader().getResourceAsStream("application.properties"))
        {
            properties.load(input);
        }
        catch (IOException e)
        {
            throw new SQLException("Errou ao tentar estabelecer ");
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String senha = properties.getProperty("db.senha");

        try
        {
            return DriverManager.getConnection(url, user, senha);
        }
        catch (SQLException e)
        {
            System.err.println("Erro ao Conectar ao Banco de Dados: "+e.getMessage());
            return null;
        }
    }
}
