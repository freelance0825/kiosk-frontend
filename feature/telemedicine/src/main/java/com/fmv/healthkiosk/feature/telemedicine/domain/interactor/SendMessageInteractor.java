package com.fmv.healthkiosk.feature.telemedicine.domain.interactor;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.SendMessageUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

public class SendMessageInteractor implements SendMessageUseCase {

    private final TelemedicineRepository repository;

    @Inject
    public SendMessageInteractor(TelemedicineRepository repository) {
        this.repository = repository;
    }

    @Override
    public Observable<ArrayList<ChatMessage>> execute(String message) {
        return repository.handleUserMessage(message);
    }
}
