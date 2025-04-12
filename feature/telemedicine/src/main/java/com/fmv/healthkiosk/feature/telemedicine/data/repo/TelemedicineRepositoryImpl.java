package com.fmv.healthkiosk.feature.telemedicine.data.repo;


import com.fmv.healthkiosk.feature.telemedicine.data.mapper.AppointmentMapper;
import com.fmv.healthkiosk.feature.telemedicine.data.mapper.DoctorMapper;
import com.fmv.healthkiosk.feature.telemedicine.data.source.local.NotificationAppointmentDataGenerator;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.TelemedicineService;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;

public class TelemedicineRepositoryImpl implements TelemedicineRepository {

    private final TelemedicineService telemedicineService;

    @Inject
    public TelemedicineRepositoryImpl(TelemedicineService telemedicineService) {
        this.telemedicineService = telemedicineService;
    }

    @Override
    public Single<List<DoctorModel>> getAvailableDoctors() {
        return telemedicineService.getAvailableDoctors()
                .map(doctorResponses -> doctorResponses.stream()
                        .map(DoctorMapper::mapToDoctorModel)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<List<AppointmentModel>> getMyAppointments(int userId) {
        return telemedicineService.getMyAppointments(userId)
                .map(doctorResponses -> doctorResponses.stream()
                        .map(AppointmentMapper::mapToAppointmentModel)
                        .collect(Collectors.toList()));
    }

    @Override
    public Single<AppointmentModel> getAppointmentsById(int appointmentId) {
        return telemedicineService.getAppointmentsById(appointmentId)
                .map(response -> AppointmentMapper.mapToAppointmentModel(response));
    }


    @Override
    public Single<AppointmentResponse> updateMyAppointments(int appointmentId, AppointmentRequest appointmentRequest) {
        return telemedicineService.updateMyAppointments(appointmentId, appointmentRequest);
    }

    @Override
    public Single<AppointmentResponse> cancelMyAppointments(int appointmentId) {
        return telemedicineService.cancelMyAppointments(appointmentId);
    }


    // CURRENTLY NOTIFICATION IS NOT IMPLEMENTED IN THE BACKEND, MAKE THE DATA STATIC FOR NOW
    @Override
    public Single<List<Notification>> getAllNotifications() {
        return Single.fromCallable(NotificationAppointmentDataGenerator::generateSampleAppointments);
    }
}