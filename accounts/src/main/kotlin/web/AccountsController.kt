package accounts.web

import accounts.model.Account
import accounts.repository.AccountRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.logging.Logger

/**
 * A RESTFul controller for accessing account information.
 *
 * @author Paul Chapman
 */
@RestController
class AccountsController @Autowired constructor(accountRepository: AccountRepository) {
    private val logger = Logger.getLogger(
        AccountsController::class.java
            .name
    )
    private val accountRepository: AccountRepository

    /**
     * Create an instance plugging in the respository of Accounts.
     *
     * @param accountRepository An account repository implementation.
     */
    init {
        this.accountRepository = accountRepository
        logger.info(
            "AccountRepository says system has "
                    + accountRepository.countAccounts() + " accounts"
        )
    }

    /**
     * Fetch an account with the specified account number.
     *
     * @param accountNumber A numeric, 9 digit account number.
     * @return The account if found.
     * @throws AccountNotFoundException If the number is not recognised.
     */
    @RequestMapping("/accounts/{accountNumber}")
    fun byNumber(@PathVariable("accountNumber") accountNumber: String): Account {
        logger.info("accounts-service byNumber() invoked: $accountNumber")
        val account = accountRepository.findByNumber(accountNumber)
        logger.info("accounts-service byNumber() found: $account")
        return account ?: throw AccountNotFoundException(accountNumber)
    }

    /**
     * Fetch accounts with the specified name. A partial case-insensitive match
     * is supported. So `http://.../accounts/owner/a` will find any
     * accounts with upper or lower case 'a' in their name.
     *
     * @param partialName
     * @return A non-null, non-empty set of accounts.
     * @throws AccountNotFoundException If there are no matches at all.
     */
    @RequestMapping("/accounts/owner/{name}")
    fun byOwner(@PathVariable("name") partialName: String): List<Account> {
        logger.info("accounts-service byOwner() invoked: ${accountRepository.javaClass.getName()} for $partialName")
        val accounts: List<Account> = accountRepository.findByOwnerContainingIgnoreCase(partialName)
        logger.info("accounts-service byOwner() found: $accounts")
        if (accounts.isEmpty()) {
            throw AccountNotFoundException(partialName)
        }
        return accounts
    }
}