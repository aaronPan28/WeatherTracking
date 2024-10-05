package com.demo.weatherTracking.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data
public class ApiErrors {
    private int errorCode;
    private String errorMessage;
    private Date timestamp;

}
