package com.fmv.healthkiosk.feature.auth.data.source.remote;

import com.fmv.healthkiosk.feature.auth.data.source.remote.model.AuthResponse;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.LoginRequest;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.RegisterRequest;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("login")
    Single<AuthResponse> loginPhoneNumber(@Body LoginRequest loginRequest);

    @POST("users/Add")
    Single<AuthResponse> register(
            @Query("name") String name,
            @Query("address") String address,
            @Query("gender") String gender,
            @Query("age") String age,
            @Query("dob") String dob,
            @Query("phoneNumber") String phoneNumber,
            @Body RegisterRequest request
    );
}
