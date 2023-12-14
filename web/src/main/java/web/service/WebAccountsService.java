package web.service;

import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import web.model.Account;

import jakarta.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Hide the access to the microservice inside this local service.
 *
 * @author Paul Chapman
 */
public class WebAccountsService {

    private final CircuitBreaker circuitBreaker;

    private final RestTemplate restTemplate;

    private final String serviceUrl;

    private final Logger logger = Logger.getLogger(WebAccountsService.class
            .getName());

    public WebAccountsService(String serviceUrl, RestTemplate restTemplate, CircuitBreakerFactory<?, ?> cbFactory) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
                : "http://" + serviceUrl;
        this.restTemplate = restTemplate;
        this.circuitBreaker = cbFactory.create("accounts");
    }


    /**
     * The RestTemplate works because it uses a custom request-factory that uses
     * Ribbon to look-up the service to use. This method simply exists to show
     * this.
     */
    @PostConstruct
    public void demoOnly() {
        // Can't do this in the constructor because the RestTemplate injection
        // happens afterwards.
        logger.warning("The RestTemplate request factory is "
                + circuitBreaker.run(restTemplate::getRequestFactory, throwable -> null));
    }

    public Account findByNumber(String accountNumber) {

        logger.info("findByNumber() invoked: for " + accountNumber);
        return circuitBreaker.run(() -> restTemplate.getForObject(serviceUrl + "/accounts/{number}",
                Account.class, accountNumber),
                throwable -> null);
    }

    public List<Account> byOwnerContains(String name) {
        logger.info("byOwnerContains() invoked:  for " + name);
        Account[] accounts = null;

        try {
            accounts = circuitBreaker.run(() -> restTemplate.getForObject(serviceUrl
                    + "/accounts/owner/{name}", Account[].class, name),
                    throwable -> null);
        } catch (HttpClientErrorException e) { // 404
            // Nothing found
        }

        if (accounts == null || accounts.length == 0) {
            return null;
        } else {
            return Arrays.asList(accounts);
        }
    }
}
