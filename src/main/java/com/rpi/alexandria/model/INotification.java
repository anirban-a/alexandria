package com.rpi.alexandria.model;

public interface INotification {
    void setRecipient(String recipient);

    String getRecipient();
    void setMessage(String message);

    String getMessage();
}
