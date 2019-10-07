package com.shcheglov.task.calculation


import com.shcheglov.task.dao.AccountDao
import com.shcheglov.task.model.Account
import com.shcheglov.task.model.Transfer
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

/**
 * @author Anton
 */
class TransferCalculatorTest extends Specification {

    AccountDao accountDao = Mock(AccountDao)

    TransferCalculator transferCalculator = new TransferCalculator(accountDao)

    @Unroll
    def "Should successfully transfer #amount from #balance1 to #balance2 and get #balanceAfter1 and #balanceAfter2 respectively"() {
        given: 'A valid transfer'
        Account account1 = new Account(id: 101L, holderName: 'TestAccount101', active: true, balance: balance1)
        Account account2 = new Account(id: 102L, holderName: 'TestAccount102', active: true, balance: balance2)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 1),
                fromAccount: account1,
                toAccount: account2,
                amount: amount,
                description: 'TestTransfer1')

        when:
        transferCalculator.accept(transfer)

        then:
        1 * accountDao.update({
            it.id == account1.id
            it.balance == balanceAfter1
        })
        1 * accountDao.update({
            it.id == account2.id
            it.balance == balanceAfter2
        })

        where:
        balance1 | balance2 | amount || balanceAfter1 | balanceAfter2
        1100     | 1200     | 100    || 1000          | 1300
        200      | 1400     | 200    || 0             | 1600
        432      | 0        | 1      || 431           | 1
    }

    def "Should not try to update account2 if failed to update account1"() {
        given: 'A valid transfer'
        Account account1 = new Account(id: 111L, holderName: 'TestAccount111', active: true, balance: 2100)
        Account account2 = new Account(id: 112L, holderName: 'TestAccount112', active: true, balance: 2200)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 2),
                fromAccount: account1,
                toAccount: account2,
                amount: 500,
                description: 'TestTransfer1')

        when:
        transferCalculator.accept(transfer)

        then:
        1 * accountDao.update({ it.id == 111L }) >> {
            throw new IllegalStateException('Intentional failure when persisting an account')
        }
        0 * accountDao.update({ it.id == 112L })

        Exception exception = thrown(IllegalStateException)
        assert exception.message.contains('Intentional failure when persisting an account')
    }

}
