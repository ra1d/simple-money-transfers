package com.shcheglov.task.validation


import com.shcheglov.task.model.Account
import com.shcheglov.task.model.Transfer
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime
import java.util.function.Consumer

/**
 * @author Anton
 */
class TransferValidatorTest extends Specification {

    Consumer<Transfer> transferValidator = new TransferValidator()

    def "Should successfully validate a transfer"() {
        given:
        Account fromAccount = new Account(id: 101L, holderName: 'TestAccount101', active: true, balance: 1100)
        Account toAccount = new Account(id: 102L, holderName: 'TestAccount102', active: true, balance: 1200)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 1),
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: 1000,
                description: 'TestTransfer1')

        when:
        transferValidator.accept(transfer)

        then:
        noExceptionThrown()
    }

    @Unroll
    def "Should throw an exception when validating a transfer involving an inactive account"() {
        given:
        Account fromAccount = new Account(id: 111L, holderName: 'TestAccount101', active: fromAccountActive, balance: 1101)
        Account toAccount = new Account(id: 112L, holderName: 'TestAccount102', active: toAccountActive, balance: 1201)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 2),
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: 1500,
                description: 'TestTransfer2')

        when:
        transferValidator.accept(transfer)

        then:
        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains(disableAccountId.toString())

        where:
        fromAccountActive|toAccountActive|disableAccountId
        true             |false          |112L
        false            |true           |111L
    }

    def "Should throw an exception when not enough funds on the source account"() {
        given:
        Account fromAccount = new Account(id: 121L, holderName: 'TestAccount121', active: true, balance: 400)
        Account toAccount = new Account(id: 122L, holderName: 'TestAccount122', active: true, balance: 1200)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 3),
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: 500,
                description: 'TestTransfer3')

        when:
        transferValidator.accept(transfer)

        then:
        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('400') && exception.message.contains('500')
    }

    @Unroll
    def "Should throw an exception when trying to transfer a non-positive amount #amountToTransfer"() {
        given:
        Account fromAccount = new Account(id: 131L, holderName: 'TestAccount121', active: true, balance: 400)
        Account toAccount = new Account(id: 132L, holderName: 'TestAccount122', active: true, balance: 1200)

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 4),
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: amountToTransfer,
                description: 'TestTransfer4')

        when:
        transferValidator.accept(transfer)

        then:
        Exception exception = thrown(expectedException)
        assert exception.message.contains("$amountToTransfer")

        where:
        amountToTransfer|expectedException
        -345L           |IllegalArgumentException
        0               |IllegalArgumentException
    }

    def "Should throw an exception when the source account is the same as the destination account"() {
        given:
        Account fromAccount = new Account(id: 141L, holderName: 'TestAccount121', active: true, balance: 500)
        Account toAccount = fromAccount

        Transfer transfer = new Transfer(
                creationTime: LocalDateTime.of(2019, 1, 2, 3, 0, 5),
                fromAccount: fromAccount,
                toAccount: toAccount,
                amount: 200,
                description: 'TestTransfer5')

        when:
        transferValidator.accept(transfer)

        then:
        Exception exception = thrown(IllegalArgumentException)
        assert exception.message.contains('141')
    }

}
