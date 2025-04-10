package net.engineeringdigest.journalApp.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;
@Getter
@Setter
@Component
public class WeatherResponse {

    private Current current;
//    public class AirQuality{
//        public String co;
//        public String no2;
//        public String o3;
//        public String so2;
//        public String pm2_5;
//        public String pm10;
//        @JsonProperty("us-epa-index")
//        public String usEpaIndex;
//        @JsonProperty("gb-defra-index")
//        public String gbDefraIndex;
//    }

//    public class Astro{
//        public String sunrise;
//        public String sunset;
//        public String moonrise;
//        public String moonset;
//        @JsonProperty("moon_phase")
//        public String moonPhase;
//        @JsonProperty("moon_illumination")
//        public int moonIllumination;
//    }

    public class Current{
        @JsonProperty("observation_time")
        public String observationTime;
        public int temperature;

        @JsonProperty("weather_descriptions")
        public List<String> weatherDescriptions;


        public int feelslike;
        @JsonProperty("uv_index")
        public int uvIndex;
        public int visibility;


    }
}
// import com.fasterxml.jackson.databind.ObjectMapper; // version 2.11.1
// import com.fasterxml.jackson.annotation.JsonProperty; // version 2.11.1
/* ObjectMapper om = new ObjectMapper();
Root root = om.readValue(myJsonString, Root.class); */



