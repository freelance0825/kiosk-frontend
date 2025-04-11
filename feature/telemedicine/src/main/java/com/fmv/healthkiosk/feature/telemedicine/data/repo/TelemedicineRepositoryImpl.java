package com.fmv.healthkiosk.feature.telemedicine.data.repo;

import android.content.Context;

import com.fmv.healthkiosk.feature.telemedicine.data.source.AppointmentDataGenerator;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.TelemedicineService;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class TelemedicineRepositoryImpl implements TelemedicineRepository {

    private final Context context;

    private final TelemedicineService telemedicineService;

    @Inject
    public TelemedicineRepositoryImpl(Context context, TelemedicineService telemedicineService) {
        this.context = context;
        this.telemedicineService = telemedicineService;

    }

    @Override
    public Single<List<Doctor>> getAvailableDoctors() {
        return telemedicineService.getAvailableDoctors();
    }

    @Override
    public Single<List<Appointment>> getMyAppointments() {
        return telemedicineService.getMyAppointments();
    }

    @Override
    public Single<List<Appointment>> getAllNotifications() {
        return Single.fromCallable(AppointmentDataGenerator::generateSampleAppointments);
    }
}