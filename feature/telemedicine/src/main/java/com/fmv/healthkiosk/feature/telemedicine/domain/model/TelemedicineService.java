package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface TelemedicineService {

    @Headers("Content-Type: application/json")
    @GET("/api/doctors/list")
    Single<List<Doctor>> getAvailableDoctors();

    @Headers("Content-Type: application/json")
    @GET("/api/appointments/list")
    Single<List<Appointment>> getMyAppointments();

}
