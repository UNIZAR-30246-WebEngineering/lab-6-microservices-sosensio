package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import web.service.WebAccountsService;

@Configuration
public class WebServerConfiguration {

    private final CircuitBreakerFactory<?, ?> cbFactory;

    /**
     * URL uses the logical name of account-service - upper or lower case,
     * doesn't matter.
     */
    private static final String ACCOUNTS_SERVICE_URL = "http://ACCOUNTS-SERVICE";

    public WebServerConfiguration(CircuitBreakerFactory<?, ?> cbFactory) {
        this.cbFactory = cbFactory;
    }


    /**
     * The AccountService encapsulates the interaction with the micro-service.
     *
     * @return A new service instance.
     */
    @Bean
    public WebAccountsService accountsService() {
        return new WebAccountsService(ACCOUNTS_SERVICE_URL, restTemplate(), cbFactory);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


    /**
     * Circuit breaker configuration
     */
    /*
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> globalCustomConfiguration() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .waitDurationInOpenState(Duration.ofMillis(1000))
                .slidingWindowSize(2)
                .build();
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(4))
                .build();

        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(timeLimiterConfig)
                .circuitBreakerConfig(circuitBreakerConfig)
                .build());
    }*/
}
