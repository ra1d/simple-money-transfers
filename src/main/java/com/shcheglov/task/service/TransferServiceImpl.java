package com.shcheglov.task.service;

import com.shcheglov.task.dao.TransferDao;
import com.shcheglov.task.model.Transfer;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Anton
 */
public class TransferServiceImpl implements TransferService {

    private final TransferDao transferDao;
    private final Consumer<Transfer> transferValidator;
    private final Consumer<Transfer> transferCalculator;

    public TransferServiceImpl(final TransferDao transferDao,
                               final Consumer<Transfer> transferValidator,
                               final Consumer<Transfer> transferCalculator) {
        this.transferDao = transferDao;
        this.transferValidator = transferValidator;
        this.transferCalculator = transferCalculator;
    }

    @Override
    public List<Transfer> getAll() {
        return transferDao.getAll();
    }

    @Override
    public Transfer getById(final Long transferId) {
        return Optional.ofNullable(transferDao.getById(transferId))
                .orElseThrow(() -> new NotFoundException("No transfer with such ID found: " + transferId));
    }

    @UnitOfWork
    @Override
    public Transfer save(final Transfer transfer) {
        return Stream.of(transfer)
                .peek(transferValidator)
                .peek(transferCalculator)
                .map(transferDao::save)
                .findAny().orElseThrow(() -> new IllegalArgumentException("Transfer should not be null!"));
    }

}
