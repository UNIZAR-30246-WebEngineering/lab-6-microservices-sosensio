package accounts.repository

import accounts.model.Account
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository

/**
 * Repository for Account data implemented using Spring Data JPA.
 *
 * @author Paul Chapman
 */
interface AccountRepository : Repository<Account, Long> {
    /**
     * Find an account with the specified account number.
     *
     * @param accountNumber
     * @return The account if found, null otherwise.
     */
    fun findByNumber(accountNumber: String): Account?

    /**
     * Find accounts whose owner name contains the specified string
     *
     * @param partialName Any alphabetic string.
     * @return The list of matching accounts - always non-null, but may be
     * empty.
     */
    fun findByOwnerContainingIgnoreCase(partialName: String): List<Account>

    /**
     * Fetch the number of accounts known to the system.
     *
     * @return The number of accounts.
     */
    @Query("SELECT count(a) from Account a")
    fun countAccounts(): Int
}