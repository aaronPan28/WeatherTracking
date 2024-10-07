package com.demo.weatherTracking.controller;

import com.demo.weatherTracking.dto.ProfileUpdateDto;
import com.demo.weatherTracking.dto.WeatherProfileDto;
import com.demo.weatherTracking.entity.CityWeather;
import com.demo.weatherTracking.entity.WeatherProfile;
import com.demo.weatherTracking.service.CityWeatherService;
import com.demo.weatherTracking.service.WeatherProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final CityWeatherService cityWeatherService;
    private final WeatherProfileService weatherProfileService;

    @Autowired
    public WeatherController(CityWeatherService cityWeatherService, WeatherProfileService weatherProfileService) {
        this.cityWeatherService = cityWeatherService;
        this.weatherProfileService = weatherProfileService;
    }

    @GetMapping("/all")
    public List<CityWeather> getAllWeatherData() {
        return cityWeatherService.getAllWeatherData();
    }

    @PostMapping("/{userId}/createProfile")
    public ResponseEntity<WeatherProfile> createProfile(@PathVariable Long userId, @RequestBody WeatherProfile profile) {
        WeatherProfile createdProfile = weatherProfileService.createProfile(userId, profile);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WeatherProfile> updateProfile(@PathVariable Long id, @RequestBody ProfileUpdateDto profile) {
        WeatherProfile updatedProfile = weatherProfileService.updateProfile(id, profile);
        return ResponseEntity.ok(updatedProfile);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        weatherProfileService.deleteProfile(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WeatherProfileDto>> getAllProfilesForUser(@PathVariable Long userId) {
        List<WeatherProfileDto> profiles = weatherProfileService.getAllProfilesForUser(userId);
        return ResponseEntity.ok(profiles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WeatherProfileDto> getProfileById(@PathVariable Long id) {
        WeatherProfileDto profile = weatherProfileService.getProfileById(id);
        return ResponseEntity.ok(profile);
    }
}