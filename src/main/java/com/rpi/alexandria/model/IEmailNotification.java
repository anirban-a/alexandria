package com.rpi.alexandria.model;

public interface IEmailNotification extends INotification {

	void setSubject(String subject);

	String getSubject();

}
