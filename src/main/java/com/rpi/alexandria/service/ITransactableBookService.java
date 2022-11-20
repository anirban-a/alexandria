package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Transactable;

import java.util.List;

public interface ITransactableBookService<T extends Transactable> {
    void createTransaction(T book);
    void deleteTransaction(T book);
    List<T> getAllTransactionsByUserId(String userId);
}
