package com.demo.weatherTracking.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProfileUpdateDto {

        private String nickname;

        private List<String> addCities;

        private List<String> removeCities;
}
