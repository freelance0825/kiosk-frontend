package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetMyAppointmentsInteractor implements GetMyAppointmentsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public GetMyAppointmentsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<Doctor>> execute() {
        return repository.getMyAppointments();
    }
}