package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import java.util.List;
import io.reactivex.Single;

public interface GetMyAppointmentsUseCase {
    Single<List<Appointment>> execute();
}