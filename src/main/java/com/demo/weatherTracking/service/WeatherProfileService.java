package com.demo.weatherTracking.service;

import com.demo.weatherTracking.dto.ProfileUpdateDto;
import com.demo.weatherTracking.dto.WeatherProfileDto;
import com.demo.weatherTracking.entity.User;
import com.demo.weatherTracking.entity.WeatherProfile;
import com.demo.weatherTracking.exceptions.EntityNotFoundException;
import com.demo.weatherTracking.exceptions.ServiceException;
import com.demo.weatherTracking.exceptions.WeatherProfileValidationException;
import com.demo.weatherTracking.repository.WeatherProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherProfileService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherProfileService.class);

    private final WeatherProfileRepository weatherProfileRepository;
    private final CityWeatherService cityWeatherService;
    private final UserService userService;

    @Autowired
    public WeatherProfileService(WeatherProfileRepository weatherProfileRepository,
                                 CityWeatherService cityWeatherService,
                                 UserService userService) {

        this.weatherProfileRepository = weatherProfileRepository;
        this.cityWeatherService = cityWeatherService;
        this.userService = userService;
    }

    @Transactional
    public WeatherProfile createProfile(Long userId, WeatherProfile profile) {

        if (profile.getNickname() == null || profile.getNickname().isEmpty()) {
            throw new WeatherProfileValidationException("Nickname must not be empty");
        }

        User user = userService.findUserById(userId);
        profile.setUser(user);
        return weatherProfileRepository.save(profile);
    }

    @Transactional
    public WeatherProfile updateProfile(Long id, ProfileUpdateDto profileUpdateDto) {
        WeatherProfile profile = weatherProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Weather profile not found with id: " + id));

        try {
            checkAndUpdateNicknameAndCities(profileUpdateDto, profile);
            profile.incrementModificationCount();
            weatherProfileRepository.save(profile);
        } catch (Exception e) {
            logger.error("Failed to update weather profile");
            throw new ServiceException("Failed to update weather profile", e);
        }

        return profile;
    }

    @Transactional
    public void deleteProfile(Long id) {
        boolean isExisted = weatherProfileRepository.existsById(id);
        if (!isExisted)
            throw new EntityNotFoundException("Weather profile not found with id: " + id + ", skip deletion");

        // delete profile and related cities from to tables
        weatherProfileRepository.deleteCitiesByProfileId(id);
        weatherProfileRepository.deleteProfileById(id);
    }

    public List<WeatherProfileDto> getAllProfilesForUser(Long userId) {
        List<WeatherProfileDto> profiles = new ArrayList<>();
        User user = userService.findUserById(userId);
        for (WeatherProfile weatherProfile : user.getProfiles()) {
            profiles.add(getProfileById(weatherProfile.getId()));
        }
        return profiles;
    }

    public WeatherProfileDto getProfileById(Long id) {
        WeatherProfile weatherProfile = weatherProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Weather profile not found with id: " + id));

        WeatherProfileDto profile = new WeatherProfileDto();
        generateProfileWithWeatherInfo(profile, weatherProfile);
        return profile;
    }

    private void generateProfileWithWeatherInfo(WeatherProfileDto profile, WeatherProfile weatherProfile) {

        profile.setId(weatherProfile.getId());
        profile.setNickname(weatherProfile.getNickname());
        profile.setUser(weatherProfile.getUser());
        profile.setCities(weatherProfile.getCities());

        // get weather info based on cities
        profile.setWeather(cityWeatherService.getCityWeatherList(profile.getCities()));
    }

    private void checkAndUpdateNicknameAndCities(ProfileUpdateDto profileUpdateDto, WeatherProfile profile) {

        // check if we need to update nickname
        if (profileUpdateDto.getNickname() != null && !profileUpdateDto.getNickname().equals(profile.getNickname())) {
            profile.setNickname(profileUpdateDto.getNickname());
        }

        List<String> existingCities = profile.getCities();
        List<String> addCities = profileUpdateDto.getAddCities();
        List<String> removeCities = profileUpdateDto.getRemoveCities();

        // check if we need to add/remove cities to the profile
        if(addCities != null){
            for(String city : addCities) {
                if (!existingCities.contains(city)) {
                    existingCities.add(city);
                }
                else {
                    logger.info("City {} already exists in {}, skip adding the city.", city, profile.getNickname());
                }
            }
        }

        // check if we need to remove cities from the profile
        if (removeCities != null) {
            for(String city : removeCities) {
                if (existingCities.contains(city)) {
                    existingCities.remove(city);
                }
                else {
                    logger.info("City {} does not exist in {}, skip removing the city.", city, profile.getNickname());
                }
            }
        }
    }
}
