package org.alexey.rentauditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// TODO Unit Tests
// TODO Swagger Open API
// TODO Dockerfile, Docker Compose
// TODO replace Russian messages with English
// TODO remove all comments
// TODO remove all empty lines, align spaces
// TODO remove all @Autowired

@SpringBootApplication
@EnableJpaAuditing
public class RentAuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RentAuditServiceApplication.class, args);
    }
}
