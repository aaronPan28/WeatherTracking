package com.demo.weatherTracking.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "city_weather")
public class CityWeather extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // main part
    private String temp;
    private String feels_like;
    private String temp_min;
    private String temp_max;
    private String pressure;
    private String humidity;
    private String sea_level;
    private String grnd_level;

    // wind.speed
    private String windSpeed;

    // weather.main
    private String weatherMain;
}
