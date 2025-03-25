package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class JournalApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(JournalApplication.class, args);
        System.out.println("The environment is ->"+run.getEnvironment().getActiveProfiles());
    }

}