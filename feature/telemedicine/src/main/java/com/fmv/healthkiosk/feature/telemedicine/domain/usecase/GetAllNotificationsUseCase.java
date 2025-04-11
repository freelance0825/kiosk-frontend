package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;

import java.util.List;

import io.reactivex.Single;

public interface GetAllNotificationsUseCase {
    Single<List<Notification>> execute();
}