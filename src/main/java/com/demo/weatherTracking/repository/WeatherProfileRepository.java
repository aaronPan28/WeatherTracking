package com.demo.weatherTracking.repository;

import com.demo.weatherTracking.entity.WeatherProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface WeatherProfileRepository extends JpaRepository<WeatherProfile, Long> {

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM weather_profile " +
            "WHERE id = :id", nativeQuery = true)
    void deleteProfileById(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM weather_profile_cities " +
            "WHERE weather_profile_id = :id", nativeQuery = true)
    void deleteCitiesByProfileId(@Param("id") Long id);
}

