package com.shcheglov.task.api

import com.shcheglov.task.BaseResourceTest
import com.shcheglov.task.model.Transfer

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import java.time.Duration
import java.time.LocalDateTime

/**
 * @author Anton
 */
class TransferResourceTest extends BaseResourceTest {

    def "Should get all transfers"() {
        when:
        Response response = target.path('/api/transfer/').request().get()
        List<Transfer> accounts = response.readEntity(List).toSorted { it.id }

        then:
        assert response.statusInfo == Response.Status.OK

        assert accounts.id == 1L..5L
        assert accounts.creationTime*.toString() == [LocalDateTime.of(2019, 2, 11, 10, 0, 1),
                                                     LocalDateTime.of(2019, 2, 12, 10, 0, 2),
                                                     LocalDateTime.of(2019, 2, 13, 10, 0, 3),
                                                     LocalDateTime.of(2019, 2, 14, 10, 0, 4),
                                                     LocalDateTime.of(2019, 2, 15, 10, 0, 5)]*.toString()
        assert accounts.fromAccount.id == [1L, 2L, 3L, 4L, 4L]
        assert accounts.toAccount.id == [2L, 3L, 4L, 1L, 2L]
        assert accounts.amount == [100L, 200L, 300L, 410L, 420L]
        assert accounts.description == ['Test transfer 1-2',
                                        'Test transfer 2-3',
                                        'Test transfer 3-4',
                                        'Test transfer 4-1',
                                        'Test transfer 4-2']
    }

    def "Should create a new transfer"() {
        when:
        Response response = target.path('/api/transfer/')
                .queryParam('fromAccount', 2L)
                .queryParam('toAccount', 1L)
                .queryParam('amount', 300L)
                .queryParam('description', 'TestTransfer_2-1')
                .request()
                .header("Content-Length", 0)
                .post(Entity.json(null))

        then:
        assert response.statusInfo == Response.Status.OK

        Transfer createdTransfer = response.readEntity(Transfer)
        assert createdTransfer.id != null
        assert Duration.between(createdTransfer.creationTime, LocalDateTime.now()).abs().toMillis() < 500L
        assert createdTransfer.fromAccount.id == 2L
        assert createdTransfer.toAccount.id == 1L
        assert createdTransfer.amount == 300L
        assert createdTransfer.description == 'TestTransfer_2-1'
    }

    def "Should get a transfer by ID"() {
        when:
        Response response = target.path("/api/transfer/4").request().get()

        then:
        assert response.statusInfo == Response.Status.OK

        Transfer responseTransfer = response.readEntity(Transfer)
        assert responseTransfer.id == 4L
        assert responseTransfer.creationTime == LocalDateTime.of(2019, 2, 14, 10, 0, 4)
        assert responseTransfer.fromAccount.id == 4L
        assert responseTransfer.toAccount.id == 1L
        assert responseTransfer.amount == 410L
        assert responseTransfer.description == 'Test transfer 4-1'
    }

    def "Should receive 404 when trying to get a non-existent transfer"() {
        given:
        String transferId = '345'

        when:
        Response response = target.path("/api/transfer/$transferId").request().get()
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.NOT_FOUND

        assert errorResponse?.code == Response.Status.NOT_FOUND.statusCode
        assert errorResponse?.message?.contains(transferId)
    }

    def "Should receive 404 when trying to make an transfer from a non-existent account"() {
        when:
        Response response = target.path('/api/transfer/')
                .queryParam('fromAccount', 1234L)
                .queryParam('toAccount', 1L)
                .queryParam('amount', 300L)
                .request()
                .header("Content-Length", 0)
                .post(Entity.json(null))
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.NOT_FOUND

        assert errorResponse?.code == Response.Status.NOT_FOUND.statusCode
        assert errorResponse?.message?.contains('1234')
    }

    def "Should receive 400 when trying to make a zero-amount transfer"() {
        when:
        Response response = target.path('/api/transfer/')
                .queryParam('fromAccount', 2L)
                .queryParam('toAccount', 1L)
                .queryParam('amount', 0L)
                .request()
                .header("Content-Length", 0)
                .post(Entity.json(null))
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.BAD_REQUEST

        assert errorResponse?.code == Response.Status.BAD_REQUEST.statusCode
        assert errorResponse?.details?.contains('should be greater than zero') && errorResponse?.details?.contains('0')
    }

}
