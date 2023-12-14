# Lab 6 Report

First of all, the config repository was forked in: https://github.com/sosensio/lab6-microservices-config-repo/

Then the following services were launched:

![Accounts and web running](images/accounts-web-running.png)

And the eureka dashboard shows both services:

![Eureka accounts web running](images/eureka-accounts-web-running.png)

The account configuration was modified to use port 3333 in commit with hash `079a34ebbf6822d815f712a08f8e5bf51df154ad` available [here](https://github.com/sosensio/lab6-microservices-config-repo/commit/079a34ebbf6822d815f712a08f8e5bf51df154ad).

At the moment we launch the second instance of the accounts service, Eureka dashboard shows both instances, one in port 2222 and the other in port 3333. 

![Eureka dashboard](images/2-account-services.png)

First account service:

![First account service](images/first-account-service.png)

Second account service:

![Second account service](images/second-account-service.png)

Furthermore, it displays the following warning:

```
EMERGENCY! EUREKA MAY BE INCORRECTLY CLAIMING INSTANCES ARE UP WHEN THEY'RE NOT. RENEWALS ARE LESSER THAN THRESHOLD AND HENCE THE INSTANCES ARE NOT BEING EXPIRED JUST TO BE SAFE.
```

When we kill the first instance of the account service, Eureka dashboard shows only the second instance in port 3333, also the warning is no longer visible.

![Eureka-accounts-3333](images/eureka-accounts-3333.png)

Now if we make a request to the web service, it successfully returns the expected result. This is due to the fact that the web service is using the Eureka service to discover the account service, and it is able to find the instance in port 3333.

![Web request](images/web-request.png)

![Account-request](images/account-request.png)
