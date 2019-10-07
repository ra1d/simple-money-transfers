package com.shcheglov.task.dao;

import com.shcheglov.task.model.Account;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

/**
 * @author Anton
 */
@Contract
public interface AccountDao {

    List<Account> getAll();

    Account save(final Account account);

    Account update(final Account account);

    Account getById(final Long accountId);

}
