package com.shcheglov.task.service


import com.shcheglov.task.calculation.TransferCalculator
import com.shcheglov.task.dao.TransferDao
import com.shcheglov.task.model.Account
import com.shcheglov.task.model.Transfer
import com.shcheglov.task.validation.TransferValidator
import spock.lang.Specification

import javax.ws.rs.NotFoundException
import java.time.LocalDateTime

/**
 * @author Anton
 */
class TransferServiceTest extends Specification {

    TransferDao transferDao = Mock(TransferDao)
    TransferValidator transferValidator = Mock(TransferValidator)
    TransferCalculator transferCalculator = Mock(TransferCalculator)

    TransferService transferService = new TransferServiceImpl(transferDao, transferValidator, transferCalculator)

    def "Should call DAO to get all transfers"() {
        when:
        transferService.getAll()

        then:
        1 * transferDao.getAll()
    }

    def "Should call DAO to get a transfer by ID"() {
        when:
        transferService.getById(123L)

        then:
        1 * transferDao.getById(123L) >> new Transfer(id: 123L)
    }

    def "Should throw an exception when trying to get a non-existent transfer"() {
        when:
        transferService.getById(234L)

        then: 'DAO cannot find a transfer with this ID'
        1 * transferDao.getById(234L) >> null

        Exception exception = thrown(NotFoundException)
        assert exception.message.contains('234')
    }

    def "Should call DAO to save a valid and calculated transfer"() {
        given:
        Account account1 = new Account(id: 101L, holderName: 'TestAccount101', active: true)
        Account account2 = new Account(id: 102L, holderName: 'TestAccount102', active: true)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 1),
                fromAccount: account1,
                toAccount: account2,
                amount: 1000,
                description: 'TestTransfer1 from 101 to 102')

        when:
        transferService.save(transfer)

        then:
        1 * transferValidator.accept(transfer) >> {}
        1 * transferCalculator.accept(transfer) >> {}
        1 * transferDao.save(transfer) >> transfer
    }

    def "Should neither call the calculator nor DAO for an invalid transfer"() {
        given:
        Account account = new Account(id: 103L, holderName: 'TestAccount103', active: true)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 2),
                fromAccount: account,
                toAccount: new Account(id: 301L),
                amount: 1500,
                description: 'TestTransfer2')

        when:
        transferService.save(transfer)

        then:
        1 * transferValidator.accept(transfer) >> {
            throw new IllegalArgumentException('INTENTIONALLY_INVALID_TRANSFER')
        }
        0 * transferCalculator.accept(transfer)
        0 * transferDao.save(_ as Transfer)

        and:
        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('INTENTIONALLY_INVALID_TRANSFER')
    }

    def "Should not call DAO to save a transfer that fails during calculation"() {
        given:
        Account account = new Account(id: 103L, holderName: 'TestAccount103', active: true)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 3),
                fromAccount: account,
                toAccount: new Account(id: 301L),
                amount: 1500,
                description: 'TestTransfer2')

        when:
        transferService.save(transfer)

        then:
        1 * transferValidator.accept(transfer) >> {}
        1 * transferCalculator.accept(transfer) >> {
            throw new IllegalStateException('INTENTIONALLY_INVALID_CALCULATION')
        }
        0 * transferDao.save(_ as Transfer)

        and:
        Exception exception = thrown(IllegalStateException)
        assert exception.message.contains('INTENTIONALLY_INVALID_CALCULATION')
    }

    def "Should throw an exception when fails to save a valid and calculated transfer"() {
        given:
        Account account1 = new Account(id: 101L, holderName: 'TestAccount101', active: true)
        Account account2 = new Account(id: 102L, holderName: 'TestAccount102', active: false)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 4),
                fromAccount: account1,
                toAccount: account2,
                amount: 1500,
                description: 'TestTransfer2')

        when:
        transferService.save(transfer)

        then:
        1 * transferValidator.accept(transfer) >> {}
        1 * transferCalculator.accept(transfer) >> {}
        1 * transferDao.save(_) >> { throw new IllegalArgumentException('INTENTIONAL_FAILURE_WHEN_SAVING') }

        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('INTENTIONAL_FAILURE_WHEN_SAVING')
    }

}
