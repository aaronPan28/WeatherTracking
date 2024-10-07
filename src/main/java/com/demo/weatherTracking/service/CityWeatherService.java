package com.demo.weatherTracking.service;

import com.demo.weatherTracking.dto.WeatherResponseDto;
import com.demo.weatherTracking.entity.CityWeather;
import com.demo.weatherTracking.exceptions.ServiceException;
import com.demo.weatherTracking.repository.CityWeatherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class CityWeatherService {

    private final CityWeatherRepository cityWeatherRepository;
    private static final Logger logger = LoggerFactory.getLogger(CityWeatherService.class);


    @Autowired
    public CityWeatherService(CityWeatherRepository cityWeatherRepository) {
        this.cityWeatherRepository = cityWeatherRepository;
    }

    @Value("${weather.api.url}")
    private String hostUrl;

    @Value("${weather.api.key}")
    private String apiKey;

    @Transactional
    @Scheduled(fixedRate = 15 * 60 * 1000) // 15 mins in milliseconds
    public void fetchAndSaveWeatherData() {
        List<String> cities = Arrays.asList("Sydney", "Melbourne", "Adelaide", "Perth", "Darwin", "Brisbane", "Canberra");

        for (String city : cities) {
            try {
                // fetch data in WeatherResponse DTO
                // it might throw RestClientException
                WeatherResponseDto response = fetchWeatherFromApi(city);

                CityWeather cityWeather = transformResponse(response);
                updateOrSaveCityWeather(cityWeather);
            } catch (RestClientException e) {
                // Log the error and handle it gracefully
                logger.error("Failed to fetch weather data");
                throw new ServiceException("Failed to fetch weather data", e);
            } catch (Exception e) {
                logger.error("Failed to update city weather");
                throw new ServiceException("Failed to update city weather", e);
            }
        }
    }

    public List<CityWeather> getAllWeatherData() {
        return cityWeatherRepository.findAll();
    }

    public List<CityWeather> getCityWeatherList(List<String> cities) {
        return cityWeatherRepository.findByNameIn(cities);
    }

    private CityWeather transformResponse(WeatherResponseDto response) {
        CityWeather cityWeather = new CityWeather();
        cityWeather.setName(response.getName());

        // main part
        WeatherResponseDto.Main main = response.getMain();
        cityWeather.setTemp(main.getTemp());
        cityWeather.setFeels_like(main.getFeels_like());
        cityWeather.setTemp_min(main.getTemp_min());
        cityWeather.setTemp_max(main.getTemp_max());
        cityWeather.setPressure(main.getPressure());
        cityWeather.setHumidity(main.getHumidity());
        cityWeather.setSea_level(main.getSea_level());
        cityWeather.setGrnd_level(main.getGrnd_level());

        // wind.speed
        cityWeather.setWindSpeed(response.getWind().getSpeed());

        // the weather array always has one object, so get weather[0].main
        cityWeather.setWeatherMain(response.getWeather()[0].getMain());

        return cityWeather;
    }

    private void updateOrSaveCityWeather(CityWeather cityWeather) throws Exception {
        // Check for existing record
        CityWeather weatherToUpdate = cityWeatherRepository.findByName(cityWeather.getName());

        if (weatherToUpdate == null) {
            // save a new record
            cityWeatherRepository.save(cityWeather);
            return;
        }

        // update the exist record
        weatherToUpdate.setName(cityWeather.getName());
        weatherToUpdate.setTemp(cityWeather.getTemp());
        weatherToUpdate.setFeels_like(cityWeather.getFeels_like());
        weatherToUpdate.setTemp_min(cityWeather.getTemp_min());
        weatherToUpdate.setTemp_max(cityWeather.getTemp_max());
        weatherToUpdate.setPressure(cityWeather.getPressure());
        weatherToUpdate.setHumidity(cityWeather.getHumidity());
        weatherToUpdate.setSea_level(cityWeather.getSea_level());
        weatherToUpdate.setGrnd_level(cityWeather.getGrnd_level());
        weatherToUpdate.setWindSpeed(cityWeather.getWindSpeed());
        weatherToUpdate.setWeatherMain(cityWeather.getWeatherMain());

        weatherToUpdate.incrementModificationCount();
        cityWeatherRepository.save(weatherToUpdate);
    }

    private WeatherResponseDto fetchWeatherFromApi(String city) throws RestClientException {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format("%s?q=%s,AU&appid=%s&units=metric", hostUrl, city, apiKey);
        return restTemplate.getForObject(url, WeatherResponseDto.class);
    }
}
