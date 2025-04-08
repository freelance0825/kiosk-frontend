package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

import java.util.List;

import io.reactivex.Single;

public interface GetAllNotificationsUseCase {
    Single<List<Appointment>> execute();
}