package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface SendMessageUseCase {

    Observable<ArrayList<ChatMessage>> execute(String message);
}
