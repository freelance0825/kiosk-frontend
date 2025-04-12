package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;

import io.reactivex.Single;

public interface UpdateMyApppointmentsUseCase {

    Single<AppointmentModel> execute(int appointmentId, AppointmentRequest appointmentRequest);

}
