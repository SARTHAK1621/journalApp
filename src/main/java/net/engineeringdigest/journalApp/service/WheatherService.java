package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.api.response.WeatherResponse;
import net.engineeringdigest.journalApp.cache.AppCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@Slf4j
public class WheatherService {
    @Value("${weather.API.key}")
    private String apiKey;

    @Autowired
    private RestTemplate restTemplate;

    //private String api="https://api.weatherstack.com/current";
    @Autowired
    AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWheather(String city) {



        try {
            WeatherResponse weatherResponse = redisService.get("Weather_of_" + city, WeatherResponse.class);
            if(weatherResponse!=null){
                return weatherResponse;
            }else{
                String finalAPI = UriComponentsBuilder.fromHttpUrl(appCache.appCache.get("weather_api"))
                        .queryParam("access_key", apiKey)
                        .queryParam("query", city)
                        .toUriString();
                ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
                WeatherResponse body = response.getBody();
                if(body!=null){
                    redisService.set("Weather_of_" + city,body,300l);
                }

                return body;
            }



        } catch (HttpClientErrorException e) {
            log.error("Error occurred while fetching weather data: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch weather data.");
        }
    }
    public WeatherResponse getWheather2(String city) {
        String finalAPI = UriComponentsBuilder.fromHttpUrl(appCache.appCache.get("weather_api"))
                .queryParam("access_key", apiKey)
                .queryParam("query", city)
                .toUriString();


        try {
           // String requestBody=
            HttpEntity<String> httpEntity=new HttpEntity<>("");
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.POST, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();

            return body;
        } catch (HttpClientErrorException e) {
            log.error("Error occurred while fetching weather data: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch weather data.");
        }
    }
}
