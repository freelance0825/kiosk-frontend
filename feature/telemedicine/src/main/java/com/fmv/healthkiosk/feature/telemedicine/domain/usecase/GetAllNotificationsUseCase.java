package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.NotificationModel;

import java.util.List;

import io.reactivex.Single;

public interface GetAllNotificationsUseCase {
    Single<List<NotificationModel>> execute(int userId);
}