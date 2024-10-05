package com.demo.weatherTracking.repository;

import com.demo.weatherTracking.entity.CityWeather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityWeatherRepository extends JpaRepository<CityWeather, Long> {

    @Query(value = "SELECT DISTINCT * FROM city_weather WHERE name = :name", nativeQuery = true)
    CityWeather findByName(@Param("name") String name);
}
