package com.example.employeeprofiles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class EmployeeProfilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeProfilesApplication.class, args);
    }

}
