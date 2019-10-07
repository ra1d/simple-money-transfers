package com.shcheglov.task.validation;

import com.shcheglov.task.model.Account;
import com.shcheglov.task.model.Transfer;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Anton
 */
public class TransferValidator implements Consumer<Transfer> {

    @Override
    public void accept(final Transfer transfer) {
        final Account fromAccount = transfer.getFromAccount();
        final Account toAccount = transfer.getToAccount();

        if (!fromAccount.isActive()) {
            throw new IllegalArgumentException("Transfer impossible, source account is not active: " + transfer.getFromAccount());
        }

        if (!toAccount.isActive()) {
            throw new IllegalArgumentException("Transfer impossible, destination account is not active: " + transfer.getToAccount());
        }

        if (Objects.equals(fromAccount.getId(), toAccount.getId())) {
            throw new IllegalArgumentException("Transfer impossible, destination account should not be equal to source account: " + transfer.getFromAccount());
        }

        final Long balance = transfer.getFromAccount().getBalance();
        final Long transferAmount = transfer.getAmount();
        if (transferAmount <= 0) {
            throw new IllegalArgumentException("Transfer impossible, amount should be greater than zero. Current amount: " + transferAmount);
        }

        if (balance < transferAmount) {
            throw new IllegalArgumentException(String.format(
                    "Transfer impossible, not enough funds on the source account." +
                            "Current balance: [%s], expected at least: [%s].", balance, transferAmount));
        }
    }

}
