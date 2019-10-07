package com.shcheglov.task.service;

import com.shcheglov.task.dao.AccountDao;
import com.shcheglov.task.model.Account;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author Anton
 */
public class AccountServiceImpl implements AccountService {

    private final AccountDao accountDao;

    public AccountServiceImpl(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public List<Account> getAll() {
        return accountDao.getAll();
    }

    @Override
    public Account getById(final Long accountId) {
        return Optional.ofNullable(accountDao.getById(accountId))
                .orElseThrow(() -> new NotFoundException("No account with such ID found: " + accountId));
    }

    @UnitOfWork
    @Override
    public Account save(final Account account) {
        return accountDao.save(account);
    }

    @UnitOfWork
    @Override
    public Account update(final Account account) {
        return accountDao.update(account);
    }

    @UnitOfWork
    @Override
    public void delete(final Long accountId) {
        Optional.ofNullable(accountDao.getById(accountId))
                .map(account -> {
                    account.setActive(false);
                    return accountDao.update(account);
                })
                .orElseThrow(() -> new IllegalArgumentException("No account with such ID found: " + accountId));
    }

}
