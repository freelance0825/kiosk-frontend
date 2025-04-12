package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;

import io.reactivex.Single;

public interface MakeAppointmentsUseCase {

    Single<AppointmentResponse> execute(int doctorId, int patientId, String doctorName, String healthComplaints, String specialization, String dateTime, String imageBase64);
}
