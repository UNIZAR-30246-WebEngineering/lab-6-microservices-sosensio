package web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Accounts web-server. Works as a microservice client, fetching data from the
 * Account-Service. Uses the Discovery Server (Eureka) to find the microservice.
 *
 * @author Paul Chapman
 */
@SpringBootApplication
public class WebServer {

    /**
     * Run the application using Spring Boot and an embedded servlet engine.
     *
     * @param args Program arguments - ignored.
     */
    public static void main(String[] args) {
        System.setProperty("spring.cloud.config.discovery.enabled", "true");
        System.setProperty("spring.cloud.config.discovery.serviceId", "configserver");
        SpringApplication.run(WebServer.class, args);
    }


}
