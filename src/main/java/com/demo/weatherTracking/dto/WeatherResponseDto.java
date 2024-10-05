package com.demo.weatherTracking.dto;

import lombok.Data;

@Data
public class WeatherResponseDto {

    private String name;
    private Main main;
    private Wind wind;
    private Weather[] weather;

    @Data
    public static class Main {
        private String temp;
        private String feels_like;
        private String temp_min;
        private String temp_max;
        private String pressure;
        private String humidity;
        private String sea_level;
        private String grnd_level;
    }

    @Data
    public static class Wind {
        private String speed;
    }

    @Data
    public static class Weather {
        private String main;
    }
}
