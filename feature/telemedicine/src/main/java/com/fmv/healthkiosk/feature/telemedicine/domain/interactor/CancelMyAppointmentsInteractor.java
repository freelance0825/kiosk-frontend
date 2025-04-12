package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;

import javax.inject.Inject;

import io.reactivex.Single;

public class CancelMyAppointmentsInteractor implements CancelMyAppointmentsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public CancelMyAppointmentsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<AppointmentModel> execute (int appointmentId) {
        return repository.cancelMyAppointments(appointmentId);
    }
}
