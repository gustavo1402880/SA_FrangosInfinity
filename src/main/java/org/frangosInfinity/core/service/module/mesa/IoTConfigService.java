package org.frangosInfinity.core.service.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.frangosInfinity.infrastructure.persistence.connection.ConnectionFactory;
import org.frangosInfinity.infrastructure.persistence.module.mesa.IoTConfigDAO;
import org.frangosInfinity.infrastructure.util.Configuracao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class IoTConfigService
{
    private final Configuracao config;

    public IoTConfigService()
    {
        this.config = Configuracao.getInstance();
    }

    public IotConfig criarConfiguracaoPadrao(Long idMesa)
    {
        String ipBase = config.getProperty("iot.ip.base", "192.168.1");
        int portaBase = config.getIntProperty("iot.porta.base", 9000);

        int numeroMesa = (idMesa.intValue() % 100) + 10;
        String ip = ipBase + "." + numeroMesa;
        int porta = portaBase + (idMesa.intValue() % 100);

        return new IotConfig(idMesa, ip, porta);
    }

    public IotConfig salvarConfiguracao(IotConfig config)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            return dao.salvar(config);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public IotConfig buscarPorId(Long id)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            return dao.buscarPorId(id).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public IotConfig buscarPorMesa(Long idMesa)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            return dao.buscarPorMesa(idMesa).orElse(null);
        }
        catch (SQLException e)
        {
            return null;
        }
    }

    public List<IotConfig> listarTodos()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            return dao.listarTodos();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public List<IotConfig> listarOnline()
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            return dao.listarOnline();
        }
        catch (SQLException e)
        {
            return List.of();
        }
    }

    public String comunicarComIoT(Long idConfig, String comando)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);

            var configOpt = dao.buscarPorId(idConfig);
            if (configOpt.isEmpty())
            {
                return "Erro IoT não encontrada";
            }

            IotConfig iotConfig = configOpt.get();

            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }

            String resposta = iotConfig.enviarComando(comando);

            if (comando.equals("STATUS"))
            {
                boolean online = resposta.startsWith("ONLINE");

                dao.atualizarStatus(idConfig, online);
            }

            return resposta;

        }
        catch (SQLException e)
        {
            return "Erro: " + e.getMessage();
        }
    }

    public boolean atualizarFirmware(Long idConfig, String versao)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);

            var configOpt = dao.buscarPorId(idConfig);
            if (configOpt.isEmpty())
            {
                return false;
            }

            dao.atualizarFirmware(idConfig, versao);

            return true;

        }
        catch (SQLException e)
        {
            return false;
        }
    }

    public boolean isOnline(Long idConfig)
    {
        try (Connection conn = ConnectionFactory.getConnection())
        {
            IoTConfigDAO dao = new IoTConfigDAO(conn);
            var configOpt = dao.buscarPorId(idConfig);
            return configOpt.isPresent() && configOpt.get().isOnline();
        }
        catch (SQLException e)
        {
            return false;
        }
    }
}
