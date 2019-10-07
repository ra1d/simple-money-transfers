package com.shcheglov.task.service;

import com.shcheglov.task.model.Transfer;

import java.util.List;


/**
 * @author Anton
 */
public interface TransferService {

    List<Transfer> getAll();

    Transfer getById(final Long transferId);

    Transfer save(final Transfer transfer);

}
