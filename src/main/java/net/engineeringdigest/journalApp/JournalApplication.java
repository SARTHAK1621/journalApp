package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableKafka
@SpringBootApplication
@EnableScheduling
public class JournalApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JournalApplication.class, args);
        System.out.println("The environment is ->"+run.getEnvironment().getActiveProfiles());
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }


}