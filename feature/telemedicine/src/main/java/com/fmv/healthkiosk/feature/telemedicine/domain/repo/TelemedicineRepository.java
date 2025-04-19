package com.fmv.healthkiosk.feature.telemedicine.domain.repo;


import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.NotificationModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface TelemedicineRepository {
    Single<List<DoctorModel>> getAvailableDoctors();

    Single<List<DoctorModel>> getLiveAvailableDoctors();

    Single<List<AppointmentModel>> getMyAppointments(int userId);

    Single<AppointmentModel> getAppointmentsById(int appointmentId);


    Single<AppointmentModel> updateMyAppointments(int appointmentId, AppointmentRequest appointmentRequest);

    Single<AppointmentModel> cancelMyAppointments(int appointmentId);

    Single<List<NotificationModel>> getPatientNotifications(int userId);

    Observable<ArrayList<ChatMessage>> handleUserMessage(String userInput);

    Single<AppointmentModel> createAppointment(int doctorId, int patientId, String doctorName, String healthComplaints, String specialization, String dateTime, String imageBase64);

    Single<DoctorTimeslotModel> getDoctorTimeslots(int doctorId, String date);
}