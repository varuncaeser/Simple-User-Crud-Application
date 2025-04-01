package com.user_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point of the User Management Spring Boot application.
 * This class is annotated with {SpringBootApplication} which is a convenience annotation that adds all the following:
 *     {@Configuration}
 *     {@EnableAutoConfiguration}
 *     {@ComponentScan}
 *
 * @author PUSHKAR D
 * @since 1.0.0
 */
@SpringBootApplication
public class UserManagementSpringBootApplication {

    /**
     * The main method to run the Spring Boot application.
     *
     * @param args The command line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(UserManagementSpringBootApplication.class, args);
    }

}
