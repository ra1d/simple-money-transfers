package com.shcheglov.task.dao;

import com.shcheglov.task.model.Transfer;

import java.util.List;

/**
 * @author Anton
 */
public interface TransferDao {

    List<Transfer> getAll();

    Transfer save(final Transfer transfer);

    Transfer getById(final Long transferId);

}
