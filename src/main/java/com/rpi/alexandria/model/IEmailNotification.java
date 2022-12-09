package com.rpi.alexandria.model;

public interface IEmailNotification extends INotification {

  String getSubject();

  void setSubject(String subject);

}
