package com.rpi.alexandria.service;

import com.rpi.alexandria.model.Exchange;

public interface IBookExchangeService extends ITransactableBookService<Exchange> {

  void rejectTransaction(String id, String username);

}
