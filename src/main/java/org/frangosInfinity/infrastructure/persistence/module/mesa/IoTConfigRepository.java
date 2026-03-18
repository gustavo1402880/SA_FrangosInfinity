package org.frangosInfinity.infrastructure.persistence.module.mesa;

import org.frangosInfinity.core.entity.module.mesa.IotConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface IoTConfigRepository extends JpaRepository<IotConfig, Long>
{
    Optional<IotConfig> findByMesaId(Long idMesa);

    List<IotConfig> findByOnlineTrue();

    @Modifying
    @Query("UPDATE IotConfig i SET i.online = :online WHERE i.id = :id")
    void atualizarStatus(@Param("id") Long id, @Param("online") boolean online);

    @Modifying
    @Query("UPDATE IotConfig i SET i.versaoFirware = :versao WHERE i.id = :id")
    void atualizarFirmware(@Param("id") Long id,@Param("versao") String versao);

    List<IotConfig> findByOnlineFalse();

    @Query("SELECT i FROM IotConfig i WHERE i.modelo LIKE %:modelo%")
    List<IotConfig> findByModeloContaning(@Param("modelo") String modelo);

    Boolean existsByMesaId(Long idMesa);
}
