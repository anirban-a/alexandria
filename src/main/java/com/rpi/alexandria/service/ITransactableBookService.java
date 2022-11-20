package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Transactable;

import java.util.List;

public interface ITransactableBookService<T extends Transactable> {
    void createTransaction(T transaction);
    void deleteTransaction(T transaction);
    List<T> getAllTransactionsByUserId(String userId);

    void markCompleted(String id, String userId);
}
