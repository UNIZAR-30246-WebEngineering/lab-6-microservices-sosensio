package accounts.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*
import java.util.logging.Logger
import javax.sql.DataSource

/**
 * The accounts web-application. This class has two uses:
 *
 *  1. Provide configuration and setup for [AccountsServer] ... or
 *  1. Run as a stand-alone Spring Boot web-application for testing (in which
 * case there is *no* microservice registration
 *
 *
 *
 * To execute as a microservice, run [AccountsServer] instead.
 *
 * @author Paul Chapman
 */
@Configuration
@PropertySource("classpath:db-config.properties")
class AccountsWebApplication {
    private val logger = Logger.getLogger(
        AccountsWebApplication::class.java
            .name
    )

    /**
     * Creates an in-memory "rewards" database populated with test data for fast
     * testing
     */
    @Bean
    fun dataSource(): DataSource {
        logger.info("dataSource() invoked")

        // Create an in-memory H2 relational database containing some demo
        // accounts.
        val dataSource: DataSource = EmbeddedDatabaseBuilder()
            .addScript("classpath:testdb/schema.sql")
            .addScript("classpath:testdb/data.sql").build()
        logger.info("dataSource = $dataSource")

        // Sanity check
        val jdbcTemplate = JdbcTemplate(dataSource)
        val accounts: List<Map<String?, Any?>> = jdbcTemplate
            .queryForList("SELECT number FROM T_ACCOUNT")
        logger.info("System has " + accounts.size + " accounts")

        // Populate with random balances
        val rand = Random()
        for (item in accounts) {
            val number = item["number"] as String?
            val balance: BigDecimal = BigDecimal.valueOf(rand.nextInt(10000000) / 100.0)
                .setScale(2, RoundingMode.HALF_UP)
            jdbcTemplate.update(
                "UPDATE T_ACCOUNT SET balance = ? WHERE number = ?",
                balance, number
            )
        }
        return dataSource
    }
}