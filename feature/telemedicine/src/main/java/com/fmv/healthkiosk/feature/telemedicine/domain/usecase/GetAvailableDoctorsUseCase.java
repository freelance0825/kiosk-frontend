package com.fmv.healthkiosk.feature.telemedicine.domain.usecase;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

import java.util.List;
import io.reactivex.Single;

public interface GetAvailableDoctorsUseCase {
   Single<List<DoctorModel>> execute();
}