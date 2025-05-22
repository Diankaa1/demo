package com.example.demo.controller;

import com.example.demo.model.CityInfo;
import com.example.demo.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cities")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    // Получить все города
    @GetMapping
    public List<CityInfo> getAllCities() {
        return cityService.getAllCities();
    }

    // Получить город по имени
    @GetMapping("/{name}")
    public CityInfo getCityByName(@PathVariable String name) {
        return cityService.getCityByName(name);
    }

    @GetMapping("/country/{country}")
    public List<CityInfo> getCitiesByCountry(@PathVariable String country) {
        return cityService.getCitiesByCountry(country);
    }

    @GetMapping("/timezone/{timezone}")
    public List<CityInfo> getCitiesByTimezone(@PathVariable String timezone) {
        return cityService.getCitiesByTimezone(timezone);
    }


    @GetMapping("/time/{name}")
    public Map<String, String> getCityTime(@PathVariable String name) {
        CityInfo city = cityService.getCityByName(name);
        if (city != null) {
            CityInfo enriched = cityService.enrichCityWithTimeDescription(city);
            return Map.of(
                    "localTime", enriched.getLocalTime(),
                    "utcTime", enriched.getUtcTime(),
                    "timeDescription", enriched.getTimeDescription()
            );
        } else {
            return Map.of("error", "City not found");
        }
    }
}