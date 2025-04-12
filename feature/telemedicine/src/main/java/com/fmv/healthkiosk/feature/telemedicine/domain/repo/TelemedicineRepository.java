package com.fmv.healthkiosk.feature.telemedicine.domain.repo;


import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;

import java.util.List;

import io.reactivex.Single;

public interface TelemedicineRepository {
    Single<List<DoctorModel>> getAvailableDoctors();

    Single<List<AppointmentModel>> getMyAppointments(int userId);


    Single<AppointmentResponse> updateMyAppointments(int appointmentId, AppointmentRequest appointmentRequest);

    Single<AppointmentResponse> cancelMyAppointments(int appointmentId);

    Single<List<Notification>> getAllNotifications();

}