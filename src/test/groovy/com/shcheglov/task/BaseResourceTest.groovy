package com.shcheglov.task

import com.shcheglov.task.SimpleMoneyTransfersApplication
import com.shcheglov.task.SimpleMoneyTransfersConfiguration
import io.dropwizard.client.JerseyClientBuilder
import io.dropwizard.testing.DropwizardTestSupport
import io.dropwizard.testing.ResourceHelpers
import spock.lang.Shared
import spock.lang.Specification

import javax.ws.rs.client.Client
import javax.ws.rs.client.WebTarget

/**
 * @author anton
 */
class BaseResourceTest extends Specification {

    public static final DropwizardTestSupport<SimpleMoneyTransfersConfiguration> INTEGRATION_TEST_ENV = new DropwizardTestSupport<>(
            SimpleMoneyTransfersApplication.class,
            ResourceHelpers.resourceFilePath("test-config.yml"))

    @Shared
    Client client

    @Shared
    WebTarget target

    def setupSpec() {
        INTEGRATION_TEST_ENV.before()
        client = new JerseyClientBuilder(INTEGRATION_TEST_ENV.getEnvironment()).build("SimpleMoneyTransfersTest")
        target = client.target('http://localhost:' + INTEGRATION_TEST_ENV.getLocalPort())
    }

    def cleanupSpec() {
        INTEGRATION_TEST_ENV.after()
    }

}
