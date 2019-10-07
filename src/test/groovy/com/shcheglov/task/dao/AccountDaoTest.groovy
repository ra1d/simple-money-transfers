package com.shcheglov.task.dao

import com.shcheglov.task.BaseDaoTest
import com.shcheglov.task.model.Account
import spock.lang.Unroll

import java.time.LocalDateTime

/**
 * @author Anton
 */
class AccountDaoTest extends BaseDaoTest {

    AccountDao accountDao = new AccountDaoImpl(entityManager)

    def "Should get all the accounts from the database"() {
        assert sql.firstRow('SELECT COUNT(*) FROM "account"').values()[0] == 4

        when:
        List<Account> allAccounts = accountDao.getAll()

        then:
        assert allAccounts?.id == [1L, 2L, 3L, 4L]
    }

    def "Should persist an account to the database"() {
        given: 'A new account'
        Account accountToPersist = new Account(
                holderName: 'Test1',
                creationTime: LocalDateTime.of(2019, 2, 1, 2, 3, 4),
                balance: 1400,
                active: true,
                description: 'TestAccount1'
        )

        when:
        Account persistedAccount = accountDao.save(accountToPersist)

        then:
        assert persistedAccount.id != null
        assert persistedAccount.holderName == accountToPersist.holderName
        assert persistedAccount.creationTime == accountToPersist.creationTime
        assert persistedAccount.balance == accountToPersist.balance
        assert persistedAccount.active == accountToPersist.active
        assert persistedAccount.description == accountToPersist.description

        and: 'Such an account is really in the persistent context'
        assert persistedAccount.properties.sort() == entityManager.find(Account, persistedAccount.id).properties.sort()
    }

    def "Should merge an account to its persisted version"() {
        assert countAccountsWithId(2) == 1

        given: 'An existing account with some of its fields updated'
        Account accountToPersist = new Account(
                id: 2L,
                holderName: 'Test Account 2',
                creationTime: LocalDateTime.of(2019, 2, 2, 2, 3, 4),
                balance: 2100,
                active: false,
                description: 'Test Account 2 closed'
        )

        when:
        Account persistedAccount = accountDao.update(accountToPersist)

        then: 'The state of the account was fully merged'
        assert persistedAccount.properties.sort() == accountToPersist.properties.sort()

        and: 'Such an account is really present in the persistent context'
        assert persistedAccount.properties.sort() == entityManager.find(Account, 2L).properties.sort()
    }

    def "Should throw an exception when trying to update a non-existent account"() {
        assert countAccountsWithId(102) == 0

        given: 'A non-existent account'
        Account accountToPersist = new Account(
                id: 102L,
                holderName: 'MadeUpAccount',
                creationTime: LocalDateTime.of(2019, 1, 1, 1, 1, 1),
                balance: 1111,
                description: 'MadeUpAccountShouldFailToSave'
        )

        when:
        accountDao.update(accountToPersist)

        then:
        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('102')
    }

    @Unroll
    def "Should get an account with the ID #accountId if it exists"() {
        when:
        Account foundAccount = accountDao.getById(accountId)

        then:
        assert foundAccount?.id == expectedAccountId

        where:
        accountId || expectedAccountId
        1L        || 1L
        2L        || 2L
        3L        || 3L
        202L      || null
        -12L      || null
    }

    private countAccountsWithId(int accountId) {
        return sql.firstRow("SELECT COUNT(*) FROM \"account\" WHERE \"id\" = $accountId").values()[0]
    }

}
