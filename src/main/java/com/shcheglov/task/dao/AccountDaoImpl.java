package com.shcheglov.task.dao;

import com.shcheglov.task.model.Account;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Anton
 */
public class AccountDaoImpl implements AccountDao {

    private final EntityManager entityManager;

    public AccountDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Account> getAll() {
        return entityManager.createQuery("SELECT a FROM com.shcheglov.task.model.Account a", Account.class).getResultList();
    }

    @Override
    public Account save(final Account account) {
        entityManager.persist(account);
        return account;
    }

    @Override
    public Account update(final Account account) {
        return Optional.ofNullable(entityManager.find(Account.class, account.getId()))
                .map(persistedAccount -> entityManager.merge(account))
                .orElseThrow(() -> new IllegalArgumentException("No such account found: " + account));
    }

    @Override
    public Account getById(final Long accountId) {
        return entityManager.find(Account.class, accountId);
    }

}
