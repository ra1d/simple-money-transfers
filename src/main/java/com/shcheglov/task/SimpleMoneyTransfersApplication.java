package com.shcheglov.task;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.shcheglov.task.api.AccountResource;
import com.shcheglov.task.api.TransferResource;
import com.shcheglov.task.api.mapping.CommonExceptionMapper;
import com.shcheglov.task.api.mapping.EntityValidationExceptionMapper;
import com.shcheglov.task.api.mapping.IllegalArgumentExceptionMapper;
import com.shcheglov.task.api.mapping.JsonProcessingExceptionMapper;
import com.shcheglov.task.calculation.TransferCalculator;
import com.shcheglov.task.dao.AccountDao;
import com.shcheglov.task.dao.AccountDaoImpl;
import com.shcheglov.task.dao.TransferDao;
import com.shcheglov.task.dao.TransferDaoImpl;
import com.shcheglov.task.model.Account;
import com.shcheglov.task.model.Transfer;
import com.shcheglov.task.service.AccountService;
import com.shcheglov.task.service.AccountServiceImpl;
import com.shcheglov.task.service.TransferService;
import com.shcheglov.task.service.TransferServiceImpl;
import com.shcheglov.task.validation.TransferValidator;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import javax.persistence.EntityManager;
import java.util.function.Consumer;

public class SimpleMoneyTransfersApplication extends Application<SimpleMoneyTransfersConfiguration> {

    private final HibernateBundle<SimpleMoneyTransfersConfiguration> hibernateBundle =
            new HibernateBundle<SimpleMoneyTransfersConfiguration>(Account.class, Transfer.class) {
                @Override
                public DataSourceFactory getDataSourceFactory(SimpleMoneyTransfersConfiguration configuration) {
                    return configuration.getDataSourceFactory();
                }
            };

    public static void main(final String[] args) throws Exception {
        new SimpleMoneyTransfersApplication().run(args);
    }

    @Override
    public void run(final SimpleMoneyTransfersConfiguration configuration,
                    final Environment environment) {
        final EntityManager entityManager = hibernateBundle.getSessionFactory().createEntityManager();

        final AccountDao accountDao = new AccountDaoImpl(entityManager);
        final TransferDao transferDao = new TransferDaoImpl(entityManager);

        final Consumer<Transfer> transferValidator = new TransferValidator();
        final Consumer<Transfer> transferCalculator = new TransferCalculator(accountDao);

        final AccountService accountService = new UnitOfWorkAwareProxyFactory(hibernateBundle).create(
                AccountServiceImpl.class, AccountDao.class, accountDao);
        final TransferService transferService = new UnitOfWorkAwareProxyFactory(hibernateBundle).create(
                TransferServiceImpl.class,
                new Class[]{TransferDao.class, Consumer.class, Consumer.class},
                new Object[]{transferDao, transferValidator, transferCalculator});

        final AccountResource accountResource = new AccountResource(accountService);
        final TransferResource transferResource = new TransferResource(transferService, accountService);

        environment.jersey().register(accountResource);
        environment.jersey().register(transferResource);
        environment.jersey().register(new CommonExceptionMapper());
        environment.jersey().register(new IllegalArgumentExceptionMapper());
        environment.jersey().register(new EntityValidationExceptionMapper());
        environment.jersey().register(new JsonProcessingExceptionMapper());
    }

    @Override
    public void initialize(final Bootstrap<SimpleMoneyTransfersConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.getObjectMapper()
                .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .registerModule(new JavaTimeModule());
    }

    @Override
    public String getName() {
        return "SimpleMoneyTransfers";
    }

}
