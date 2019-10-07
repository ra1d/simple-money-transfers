package com.shcheglov.task.service;

import com.shcheglov.task.model.Account;

import java.util.List;

/**
 * @author Anton
 */
public interface AccountService {

    List<Account> getAll();

    Account getById(final Long accountId);

    Account save(final Account account);

    Account update(final Account account);

    void delete(final Long accountId);

}
