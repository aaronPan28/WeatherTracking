package com.demo.weatherTracking.dto;

import com.demo.weatherTracking.entity.CityWeather;
import com.demo.weatherTracking.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class WeatherProfileDto {

    private Long id;
    private String nickname;
    private User user;
    private List<String> cities;
    private List<CityWeather> weather;
}
