package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;

import javax.inject.Inject;

import io.reactivex.Single;

public class UpdateMyAppointmentsInteractor implements UpdateMyApppointmentsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public UpdateMyAppointmentsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<AppointmentResponse> execute (int appointmentId, AppointmentRequest request) {
        return repository.updateMyAppointments(appointmentId, request);
    }

}
