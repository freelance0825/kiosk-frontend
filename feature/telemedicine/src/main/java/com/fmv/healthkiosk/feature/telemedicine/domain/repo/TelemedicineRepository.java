package com.fmv.healthkiosk.feature.telemedicine.domain.repo;


import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

import java.util.List;

import io.reactivex.Single;

public interface TelemedicineRepository {
    Single<List<DoctorModel>> getAvailableDoctors();

    Single<List<AppointmentModel>> getMyAppointments(int userId);

    Single<List<Notification>> getAllNotifications();

}