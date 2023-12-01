package web.service;

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

    private final RestTemplate restTemplate;

    private final String serviceUrl;

    private final Logger logger = Logger.getLogger(WebAccountsService.class
            .getName());

    public WebAccountsService(String serviceUrl, RestTemplate restTemplate) {
        this.serviceUrl = serviceUrl.startsWith("http") ? serviceUrl
                : "http://" + serviceUrl;
        this.restTemplate = restTemplate;
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
                + restTemplate.getRequestFactory());
    }

    public Account findByNumber(String accountNumber) {

        logger.info("findByNumber() invoked: for " + accountNumber);
        return restTemplate.getForObject(serviceUrl + "/accounts/{number}",
                Account.class, accountNumber);
    }

    public List<Account> byOwnerContains(String name) {
        logger.info("byOwnerContains() invoked:  for " + name);
        Account[] accounts = null;

        try {
            accounts = restTemplate.getForObject(serviceUrl
                    + "/accounts/owner/{name}", Account[].class, name);
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
