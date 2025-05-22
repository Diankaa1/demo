package com.example.demo.service;

import com.example.demo.model.CityInfo;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CityService {

    private final List<CityInfo> cities = new ArrayList<>();

    @PostConstruct
    public void init() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getClassLoader().getResourceAsStream("cities.csv"), StandardCharsets.UTF_8))) {
            String line;
            reader.readLine(); // пропустить заголовок
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    cities.add(new CityInfo(
                            parts[0],
                            parts[1],
                            Double.parseDouble(parts[2]),
                            Double.parseDouble(parts[3]),
                            parts[4],
                            null, // временно, заполним позже
                            null,
                            null // timeDescription
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<CityInfo> getAllCities() {
        return enrichCitiesWithTime(cities);
    }

    public CityInfo getCityByName(String name) {
        return cities.stream()
                .filter(c -> c.getCity().equalsIgnoreCase(name))
                .map(this::enrichCityWithTime)
                .findFirst()
                .orElse(null);
    }

    public List<CityInfo> getCitiesByCountry(String country) {
        return cities.stream()
                .filter(c -> c.getCountry().equalsIgnoreCase(country))
                .map(this::enrichCityWithTime)
                .collect(Collectors.toList());
    }

    public List<CityInfo> getCitiesByTimezone(String timezone) {
        return cities.stream()
                .filter(c -> c.getTimezone().startsWith(timezone)) // проверяем начало временной зоны
                .map(this::enrichCityWithTimeDescription) // обогащаем информацией о времени
                .collect(Collectors.toList());
    }


    private List<CityInfo> enrichCitiesWithTime(List<CityInfo> cityList) {
        List<CityInfo> updated = new ArrayList<>();
        for (CityInfo city : cityList) {
            updated.add(enrichCityWithTime(city));
        }
        return updated;
    }

    private CityInfo enrichCityWithTime(CityInfo city) {
        try {
            ZonedDateTime now = ZonedDateTime.now(ZoneId.of(city.getTimezone()));
            city.setLocalTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            city.setUtcTime(Instant.now().toString()); // UFC-формат (ISO 8601, UTC)
        } catch (Exception e) {
            city.setLocalTime("Unknown");
            city.setUtcTime("Unknown");
        }
        return city;
    }

    public CityInfo enrichCityWithTimeDescription(CityInfo city) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of(city.getTimezone()));
        city.setLocalTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        city.setUtcTime(Instant.now().toString()); // UFC-формат (ISO 8601, UTC)
        city.setTimeDescription(String.format("%s: %s (+%s UTC)",
                city.getCity(),
                now.format(DateTimeFormatter.ofPattern("HH:mm")),
                now.getOffset().getTotalSeconds() / 3600));
        return city;
    }

}