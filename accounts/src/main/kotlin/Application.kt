package accounts

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccountsServer

fun main(args: Array<String>) {
    System.setProperty("spring.cloud.config.discovery.enabled", "true")
    System.setProperty("spring.cloud.config.discovery.serviceId", "configserver")
    runApplication<AccountsServer>(*args)
}