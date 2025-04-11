package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAvailableDoctorsUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetAvailableDoctorsInteractor implements GetAvailableDoctorsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public GetAvailableDoctorsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<DoctorModel>> execute() {
        return repository.getAvailableDoctors();
    }
}