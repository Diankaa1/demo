package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityInfo {
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private String timezone;
    private String localTime;  // текущее местное время
    private String utcTime;    // текущее UTC время в формате ISO 8601
    private String timeDescription;

}