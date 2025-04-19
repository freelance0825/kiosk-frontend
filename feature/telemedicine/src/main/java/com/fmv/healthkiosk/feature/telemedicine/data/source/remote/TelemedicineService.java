package com.fmv.healthkiosk.feature.telemedicine.data.source.remote;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.DoctorResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.DoctorTimeslotResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.MakeAppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.NotificationResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TelemedicineService {

    @Headers("Content-Type: application/json")
    @GET("doctors/list")
    Single<List<DoctorResponse>> getAvailableDoctors();

    @Headers("Content-Type: application/json")
    @GET("appointments/patient/{id}")
    Single<List<AppointmentResponse>> getMyAppointments(
            @Path("id") int userId
    );

    @Headers("Content-Type: application/json")
    @GET("appointments/{id}")
    Single<AppointmentResponse> getAppointmentsById(
            @Path("id") int appointmentId
    );

    @Headers("Content-Type: application/json")
    @PUT("appointments/{id}")
    Single<AppointmentResponse> updateMyAppointments(
            @Path("id") int appointmentId,
            @Body AppointmentRequest appointmentRequest
    );

    @Headers("Content-Type: application/json")
    @DELETE("appointments/{id}")
    Single<AppointmentResponse> cancelMyAppointments(@Path("id") int appointmentId);

    @POST("appointments/add")
    Single<AppointmentResponse> createAppointment(
            @Query("currentPatient") long currentPatient,
            @Query("chosenDoctor") long chosenDoctor,
            @Body MakeAppointmentRequest makeAppointmentRequest
    );

    @Headers("Content-Type: application/json")
    @GET("notifications/patient/{id}")
    Single<List<NotificationResponse>> getPatientNotifications(@Path("id") int patientId, @Query("id") long currentPatient);

    @Headers("Content-Type: application/json")
    @GET("appointments/timeslots/{doctorId}")
    Single<DoctorTimeslotResponse> getDoctorTimeslots(
            @Path("doctorId") int doctorId,
            @Query("date") String date
    );
}
