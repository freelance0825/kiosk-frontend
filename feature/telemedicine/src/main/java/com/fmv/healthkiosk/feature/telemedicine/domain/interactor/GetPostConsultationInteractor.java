package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetPostConsultationUseCase;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetPostConsultationInteractor implements GetPostConsultationUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public GetPostConsultationInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<AppointmentModel> execute(int appointmentId) {
        return repository.getAppointmentsById(appointmentId);
    }
}
