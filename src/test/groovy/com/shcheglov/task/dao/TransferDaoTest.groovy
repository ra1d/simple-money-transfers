package com.shcheglov.task.dao

import com.shcheglov.task.BaseDaoTest
import com.shcheglov.task.model.Account
import com.shcheglov.task.model.Transfer
import spock.lang.Unroll

import java.time.LocalDateTime

/**
 * @author Anton
 */
class TransferDaoTest extends BaseDaoTest {

    TransferDao transferDao = new TransferDaoImpl(entityManager)

    def "Should get all the transfers from the database"() {
        assert sql.firstRow('SELECT COUNT(*) FROM "transfer"').values()[0] == 5

        when:
        List<Transfer> allTransfers = transferDao.getAll()

        then:
        assert allTransfers?.id == 1L..5L
    }

    def "Should persist a transfer to the database"() {
        given: 'A new transfer'
        Transfer transferToPersist = new Transfer(
                creationTime: LocalDateTime.of(2019, 3, 1, 1, 2, 3),
                fromAccount: new Account(id: 1L),
                toAccount: new Account(id: 2L),
                amount: 500,
                description: 'TestTransfer1')

        when:
        Transfer persistedTransfer = transferDao.save(transferToPersist)

        then:
        assert persistedTransfer.id != null
        assert persistedTransfer.creationTime == transferToPersist.creationTime
        assert persistedTransfer.fromAccount == transferToPersist.fromAccount
        assert persistedTransfer.toAccount == transferToPersist.toAccount
        assert persistedTransfer.amount == transferToPersist.amount
        assert persistedTransfer.description == transferToPersist.description

        and: 'Such a transfer is really present in the persistent context'
        assert persistedTransfer.properties.sort() == entityManager.find(Transfer, persistedTransfer.id).properties.sort()
    }

    @Unroll
    def "Should get a transfer with the ID #transferId if it exists"() {
        when:
        Transfer foundTransfer = transferDao.getById(transferId)

        then:
        assert foundTransfer?.id == expectedTransferId

        where:
        transferId || expectedTransferId
        1L         || 1L
        2L         || 2L
        3L         || 3L
        204L       || null
        -14L       || null
    }

}
