package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;

import java.util.List;
import io.reactivex.Single;

public interface GetMyAppointmentsUseCase {
    Single<List<AppointmentModel>> execute(int userId);
}