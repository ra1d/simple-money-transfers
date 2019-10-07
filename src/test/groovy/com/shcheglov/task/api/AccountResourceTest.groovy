package com.shcheglov.task.api

import com.shcheglov.task.BaseResourceTest
import com.shcheglov.task.model.Account

import javax.ws.rs.client.Entity
import javax.ws.rs.core.Response
import java.time.LocalDateTime

import static javax.ws.rs.core.MediaType.APPLICATION_JSON

/**
 * @author Anton
 */
class AccountResourceTest extends BaseResourceTest {

    def "Should get all accounts"() {
        when:
        Response response = target.path('/api/account/').request().get()
        List<Account> accounts = response.readEntity(List).toSorted { it.id }

        then:
        assert response.statusInfo == Response.Status.OK

        assert accounts.id == 1L..4L
        assert accounts.holderName == 'Test Account 1'..'Test Account 4'
        assert accounts.creationTime*.toString() == [LocalDateTime.of(2019, 2, 1, 1, 2, 3),
                                                     LocalDateTime.of(2019, 2, 2, 2, 3, 4),
                                                     LocalDateTime.of(2019, 2, 3, 3, 4, 5),
                                                     LocalDateTime.of(2019, 2, 4, 4, 5, 6)]*.toString()
        assert accounts.balance == (1000L..4000L).step(1000)
        assert accounts.active == [true, true, false, true]
        assert accounts.description == ['Test account 1 description', 'Test account 2 description',
                                        'Test account 3 description - disabled', 'Test account 4 description']
    }

    def "Should create an account"() {
        given:
        Account newAccount = new Account(
                holderName: 'NewTest1',
                creationTime: LocalDateTime.of(2019, 3, 1, 1, 2, 3),
                balance: 1100,
                active: true,
                description: 'NewTestAccount1')

        when:
        Response response = target.path('/api/account/').request().post(Entity.entity(newAccount, APPLICATION_JSON))

        then:
        assert response.statusInfo == Response.Status.OK

        Account createdAccount = response.readEntity(Account)
        assert createdAccount.id != null
        assert createdAccount.holderName == newAccount.holderName
        assert createdAccount.creationTime == newAccount.creationTime
        assert createdAccount.balance == newAccount.balance
        assert createdAccount.active
        assert createdAccount.description == newAccount.description
    }

    def "Should get account by ID"() {
        when:
        Response response = target.path("/api/account/2").request().get()
        Account responseAccount = response.readEntity(Account)

        then:
        assert response.statusInfo == Response.Status.OK

        assert responseAccount.id == 2L
        assert responseAccount.holderName == 'Test Account 2'
        assert responseAccount.creationTime == LocalDateTime.of(2019, 2, 2, 2, 3, 4)
        assert responseAccount.balance == 2000L
        assert responseAccount.active
        assert responseAccount.description == 'Test account 2 description'
    }

    def "Should receive 404 when trying to get a non-existent account by ID"() {
        given:
        String accountId = '123'

        when:
        Response response = target.path("/api/account/$accountId").request().get()
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.NOT_FOUND
        assert errorResponse?.code == Response.Status.NOT_FOUND.statusCode
        assert errorResponse?.message?.contains(accountId)
    }

    def "Should update an account"() {
        given:
        Account updatedAccount = new Account(
                id: 3L,
                holderName: 'Test Account 3',
                creationTime: LocalDateTime.of(2019, 2, 3, 3, 4, 5),
                balance: 3500L,
                active: true,
                description: 'Test account 3 description - enabled')

        when:
        Response response = target.path('/api/account/').request().put(Entity.entity(updatedAccount, APPLICATION_JSON))

        then:
        assert response.statusInfo == Response.Status.OK

        Account persistedAccount = response.readEntity(Account)
        assert persistedAccount.id == updatedAccount.id
        assert persistedAccount.holderName == updatedAccount.holderName
        assert persistedAccount.creationTime == updatedAccount.creationTime
        assert persistedAccount.balance == updatedAccount.balance
        assert persistedAccount.active
        assert persistedAccount.description == updatedAccount.description
    }

    def "Should receive 400 when trying to update a non-existent account"() {
        given:
        Account updatedAccount = new Account(
                id: 303L,
                holderName: 'Test Account 3',
                creationTime: LocalDateTime.of(2019, 3, 3, 3, 4, 5),
                balance: 3300L,
                active: true,
                description: 'Test account 3 description - enabled')

        when:
        Response response = target.path("/api/account/").request().put(Entity.entity(updatedAccount, APPLICATION_JSON))
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.BAD_REQUEST
        assert errorResponse?.code == Response.Status.BAD_REQUEST.statusCode
    }

    def "Should remove an account by ID by making it inactive"() {
        given:
        Long accountId = 1L

        when: "Deleting the account"
        Response response = target.path("/api/account/$accountId").request().delete()

        then:
        assert response?.statusInfo == Response.Status.NO_CONTENT

        when: "Checking its state again"
        Account inactiveAccount = target.path("/api/account/$accountId").request().get(Account)

        then:
        assert inactiveAccount.id == accountId
        assert !inactiveAccount.active
    }

    def "Should receive 400 when trying to delete a non-existent account"() {
        given:
        String accountNumber = '234'

        when:
        Response response = target.path("/api/account/$accountNumber").request().delete()
        def errorResponse = response.readEntity(Object)

        then:
        assert response.statusInfo == Response.Status.BAD_REQUEST
        assert errorResponse?.code == Response.Status.BAD_REQUEST.statusCode
    }

}
