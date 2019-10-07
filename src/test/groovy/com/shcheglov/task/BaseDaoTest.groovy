package com.shcheglov.task

import groovy.sql.Sql
import org.hibernate.jpa.HibernatePersistenceProvider
import spock.lang.Shared
import spock.lang.Specification

import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.persistence.spi.PersistenceProvider

/**
 * @author Anton
 */
class BaseDaoTest extends Specification {

    @Shared
    private EntityManagerFactory entityManagerFactory

    @Shared
    EntityManager entityManager

    @Shared
    Sql sql

    def setupSpec() {
        String dbUrl = 'jdbc:h2:mem:smtdb-test;DATABASE_TO_UPPER=false;AUTOCOMMIT=true'

        def persistenceProperties = [
                'javax.persistence.jdbc.driver'  : 'org.h2.Driver',
                'javax.persistence.jdbc.url'     : dbUrl,
                'javax.persistence.jdbc.user'    : 'sa',
                'javax.persistence.jdbc.password': '',
                'hibernate.hbm2ddl.auto'         : 'create-drop',
                'hibernate.hbm2ddl.import_files' : 'init_data.sql'
        ]

        PersistenceProvider hibernatePersistenceProvider = new HibernatePersistenceProvider()
        entityManagerFactory = hibernatePersistenceProvider.createEntityManagerFactory('smtEMF', persistenceProperties)
        entityManager = entityManagerFactory.createEntityManager(persistenceProperties)
        sql = Sql.newInstance(dbUrl, 'sa', '', 'org.h2.Driver')
    }

    def cleanupSpec() {
        sql?.close()
        entityManager?.close()
        entityManagerFactory?.close()
    }

}
