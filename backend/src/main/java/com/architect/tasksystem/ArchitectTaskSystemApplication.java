package com.architect.tasksystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArchitectTaskSystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ArchitectTaskSystemApplication.class, args);
    }
}
