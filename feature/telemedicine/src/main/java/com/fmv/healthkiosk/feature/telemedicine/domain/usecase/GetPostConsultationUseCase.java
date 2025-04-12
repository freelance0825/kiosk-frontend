package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;

import io.reactivex.Single;

public interface GetPostConsultationUseCase {

    Single<AppointmentModel> execute(int appointmentId);
}
