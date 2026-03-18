package org.frangosInfinity.infrastructure.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class Geolocalizador
{
    private static final Map<Integer, Coordenadas> cacheLocalizacao = new HashMap<>();

    @Value("${geo.latitude.base:-26.4911}")
    private double latitudeBase;

    @Value("${geo.longitude.base:-49.0771}")
    private double longitudeBase;

    public Coordenadas getCoordenadasMesa(int idMesa)
    {
        if (cacheLocalizacao.containsKey(idMesa))
        {
            return cacheLocalizacao.get(idMesa);
        }

        ThreadLocalRandom random = ThreadLocalRandom.current();
        double variacaoLat = random.nextDouble(-0.01, 0.01);
        double variacaoLon = random.nextDouble(-0.01, 0.01);

        variacaoLat += (idMesa * 0.0001) % 0.005;
        variacaoLon += (idMesa * 0.0001) % 0.005;

        Coordenadas coord = new Coordenadas(
                latitudeBase + variacaoLat,
                longitudeBase + variacaoLon,
                idMesa
        );

        cacheLocalizacao.put(idMesa, coord);
        return coord;
    }

    public double calcularDistancia(int idMesa1, int idMesa2)
    {
        Coordenadas c1 = getCoordenadasMesa(idMesa1);
        Coordenadas c2 = getCoordenadasMesa(idMesa2);

        return calcularDistanciaHaversine(
                c1.latitude, c1.longitude,
                c2.latitude, c2.longitude
        );
    }

    private double calcularDistanciaHaversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000;

        double lat1Rad = Math.toRadians(lat1);
        double lat2Rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public boolean estaNoRaio(int idMesa1, int idMesa2, double raioMetros)
    {
        double distancia = calcularDistancia(idMesa1, idMesa2);
        return distancia <= raioMetros;
    }

    public static class Coordenadas
    {
        public final double latitude;
        public final double longitude;
        public final int idMesa;
        public final String timestamp;

        public Coordenadas(double latitude, double longitude, int idMesa)
        {
            this.latitude = latitude;
            this.longitude = longitude;
            this.idMesa = idMesa;
            this.timestamp = java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")
            );
        }

        public String formatar()
        {
            return String.format("%.6f, %.6f", latitude, longitude);
        }

        public String getGoogleMapsLink()
        {
            return String.format("https://www.google.com/maps?q=%.6f,%.6f", latitude, longitude);
        }

        @Override
        public String toString()
        {
            return String.format("Mesa %d: %s (atualizado às %s)",
                    idMesa, formatar(), timestamp);
        }
    }
}
