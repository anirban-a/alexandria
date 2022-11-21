package com.rpi.alexandria.service;

import com.rpi.alexandria.model.INotification;

public interface INotificationService<T extends INotification> {

	void sendNotification(T notification);

}
