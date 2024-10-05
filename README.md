# Weather Tracking Application

This is a Spring Boot application for tracking weather in major cities in Australia. Users can create profiles to track multiple cities and get current weather data from the OpenWeather API.

## Technical Requirements

- **Language**: Java
- **Framework**: Spring Boot 3.x
- **Database**: MySQL
- **Other Libraries**: Spring Data JPA, Spring Web, Lombok, RestTemplate, etc.

## Features

1. User registration
2. Manage weather profiles
3. Automatic weather data fetching every 15 minutes
4. Persisting weather data in a MySQL database
5. Auditing for weather profiles

## Setup and Running the Application

### Prerequisites

1. **Java 17 or higher**: Ensure you have JDK installed.
2. **Maven**: Make sure Maven is installed.
3. **MySQL**: Install MySQL and create a database named `weatherdb`.
4. **Update Config**: Update related fields (spring.datasource.username and spring.datasource.password) in application.properties
5. **Update API Key for OpenWeather**: register a dev account [here](https://openweathermap.org) and update weather.api.key. Feel free to use mine :)

### Steps to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/weather-tracking-app.git
   cd weather-tracking-app
   ```
2. Download dependencies use Maven:
   ```bash
   mvn clean install
   ```
3. Run the project:
   ```bash
    mvn spring-boot:run
   ```
4. Run http endpoints mentioned in testWeather.http