package com.wpl.volunteer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VolunteerApplication {

    public static void main(String[] args) {
        SpringApplication.run(VolunteerApplication.class, args);
    }

}
