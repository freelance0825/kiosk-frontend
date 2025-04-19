package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;

import java.util.List;

import io.reactivex.Single;

public interface GetDoctorTimeslotsUseCase {
   Single<DoctorTimeslotModel> execute(int doctorId, String date);
}