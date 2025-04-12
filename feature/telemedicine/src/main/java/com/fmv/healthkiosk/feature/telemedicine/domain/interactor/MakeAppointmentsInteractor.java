package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.MakeAppointmentsUseCase;

import javax.inject.Inject;

import io.reactivex.Single;

public class MakeAppointmentsInteractor implements MakeAppointmentsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public MakeAppointmentsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<AppointmentModel> execute(int doctorId, int patientId, String doctorName, String healthComplaints, String specialization, String dateTime, String imageBase64) {
        return repository.createAppointment(doctorId, patientId, doctorName, healthComplaints, specialization, dateTime, imageBase64);
    }
}
