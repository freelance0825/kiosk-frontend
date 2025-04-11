package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAllNotificationsUseCase;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class GetAllNotificationsInteractor implements GetAllNotificationsUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public GetAllNotificationsInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Single<List<Notification>> execute() {
        return repository.getAllNotifications();
    }
}