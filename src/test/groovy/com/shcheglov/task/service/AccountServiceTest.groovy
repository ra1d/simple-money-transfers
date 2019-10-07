package com.shcheglov.task.service


import com.shcheglov.task.dao.AccountDao
import com.shcheglov.task.model.Account
import spock.lang.Specification

import javax.ws.rs.NotFoundException
import java.time.LocalDateTime

/**
 * @author Anton
 */
class AccountServiceTest extends Specification {

    AccountDao accountDao = Mock(AccountDao)

    AccountService accountService = new AccountServiceImpl(accountDao)

    def "Should call DAO to get all accounts"() {
        when:
        accountService.getAll()

        then:
        1 * accountDao.getAll()
    }

    def "Should call DAO to get an account by ID"() {
        when:
        accountService.getById(456L)

        then:
        1 * accountDao.getById(456L) >> new Account(id: 456L, holderName: 'TestHolder')
    }

    def "Should throw an exception when getting a non-existent account"() {
        when:
        accountService.getById(567L)

        then: 'DAO cannot find an account with this ID'
        1 * accountDao.getById(567L) >> null

        Exception exception = thrown(NotFoundException)
        assert exception.message.contains('567')
    }

    def "Should call DAO to save an account"() {
        given:
        Account account = new Account(
                holderName: 'Test1',
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 1),
                balance: 1100,
                active: true,
                description: 'TestAccount1')

        when:
        accountService.save(account)

        then:
        1 * accountDao.save(account)
    }

    def "Should call DAO to update an account"() {
        given:
        Account updatedAccount = new Account(
                id: 202L,
                holderName: 'Test2',
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 2),
                balance: 1200,
                active: false,
                description: 'TestAccount2')

        when:
        accountService.update(updatedAccount)

        then:
        1 * accountDao.update(updatedAccount)
    }

    def "Should re-throw an exception from DAO"() {
        given: 'DAO throws an exception'
        Account updatedAccount = new Account(
                id: 203L,
                holderName: 'Test3',
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 3),
                balance: 1300,
                active: true,
                description: 'TestAccount3')

        when:
        accountService.update(updatedAccount)

        then:
        1 * accountDao.update({ it.id == 203L }) >> { throw new IllegalStateException('_EXPECTED_EXCEPTION_') }

        Exception exception = thrown(IllegalStateException)
        assert exception.message.contains('_EXPECTED_EXCEPTION_')
    }

    def "Should call DAO to update an account with the given ID when deleting the account"() {
        given:
        Account existingAccount = new Account(
                id: 678L,
                holderName: 'Test678',
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 4),
                balance: 0,
                active: true,
                description: 'TestAccount4')

        Account deactivatedAccount = existingAccount.tap { active = false }

        accountDao.getById(678L) >> existingAccount

        when:
        accountService.delete(678L)

        then:
        1 * accountDao.update({ it.id == 678L && it.active == false }) >> deactivatedAccount
    }

    def "Should throw an exception when deleting a non-existent account"() {
        given: 'DAO cannot find an account with this ID'
        accountDao.getById(789L) >> null

        when:
        accountService.delete(789L)

        then: 'no attempt to update any account'
        0 * accountDao.update(_)

        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('789')
    }

}
