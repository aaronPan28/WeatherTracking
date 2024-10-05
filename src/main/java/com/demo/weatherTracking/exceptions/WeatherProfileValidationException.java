package com.demo.weatherTracking.exceptions;

public class WeatherProfileValidationException extends RuntimeException {
    public WeatherProfileValidationException(String message) {
        super(message);
    }
}
