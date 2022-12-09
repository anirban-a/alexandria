package com.rpi.alexandria.model;

public interface INotification {

  String getRecipient();

  void setRecipient(String recipient);

  String getMessage();

  void setMessage(String message);

}
