package com.fmv.healthkiosk.feature.telemedicine.data.repo;

import com.fmv.healthkiosk.feature.telemedicine.data.source.DoctorDataGenerator;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class TelemedicineRepositoryImpl implements TelemedicineRepository {

    @Override
    public Single<List<Doctor>> getMyAppointments() {
        return Single.fromCallable(DoctorDataGenerator::generateSampleDoctors);
    }

    @Override
    public Single<List<Doctor>> getAvailableDoctors() {
        return Single.fromCallable(DoctorDataGenerator::generateSampleDoctors);
    }
}