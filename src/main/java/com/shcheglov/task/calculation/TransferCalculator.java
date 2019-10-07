package com.shcheglov.task.calculation;

import com.shcheglov.task.dao.AccountDao;
import com.shcheglov.task.model.Account;
import com.shcheglov.task.model.Transfer;

import java.util.function.Consumer;

/**
 * @author Anton
 */
public class TransferCalculator implements Consumer<Transfer> {

    private final AccountDao accountDao;

    public TransferCalculator(final AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public void accept(Transfer transfer) {
        final Account fromAccount = transfer.getFromAccount();
        fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());

        final Account toAccount = transfer.getToAccount();
        toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());

        accountDao.update(fromAccount);
        accountDao.update(toAccount);
    }

}
