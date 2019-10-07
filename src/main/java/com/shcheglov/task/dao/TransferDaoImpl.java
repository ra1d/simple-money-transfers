package com.shcheglov.task.dao;

import com.shcheglov.task.model.Transfer;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * @author Anton
 */
public class TransferDaoImpl implements TransferDao {

    private final EntityManager entityManager;

    public TransferDaoImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Transfer> getAll() {
        return entityManager.createQuery("SELECT t FROM com.shcheglov.task.model.Transfer t", Transfer.class).getResultList();
    }

    @Override
    public Transfer save(final Transfer transfer) {
        return Optional.ofNullable(entityManager.unwrap(Session.class))
                .map(session -> session.save(transfer))
                .map(newTransferId -> entityManager.find(Transfer.class, newTransferId))
                .orElseThrow(() -> new IllegalArgumentException("Failed saving the transfer: " + transfer));
    }

    @Override
    public Transfer getById(final Long transferId) {
        return entityManager.find(Transfer.class, transferId);
    }

}
