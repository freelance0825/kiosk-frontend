package com.fmv.healthkiosk.feature.telemedicine.data.source.remote;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.DoctorResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface TelemedicineService {

    @Headers("Content-Type: application/json")
    @GET("doctors/list")
    Single<List<DoctorResponse>> getAvailableDoctors();

    @Headers("Content-Type: application/json")
    @GET("appointments/patient/{id}")
    Single<List<AppointmentResponse>> getMyAppointments(
            @Path("id") int userId
    );
}
