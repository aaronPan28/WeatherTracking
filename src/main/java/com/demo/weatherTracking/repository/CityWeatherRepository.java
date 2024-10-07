package com.demo.weatherTracking.repository;

import com.demo.weatherTracking.entity.CityWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityWeatherRepository extends JpaRepository<CityWeather, Long> {

    CityWeather findByName(String name);

    List<CityWeather> findByNameIn(List<String> names);
}
