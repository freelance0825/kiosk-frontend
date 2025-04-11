package com.fmv.healthkiosk.feature.telemedicine.domain.repo;


import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

import java.util.List;

import io.reactivex.Single;

public interface TelemedicineRepository {
    Single<List<Doctor>> getAvailableDoctors();

    Single<List<Appointment>> getMyAppointments();

    Single<List<Appointment>> getAllNotifications();

}