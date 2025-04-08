package com.fmv.healthkiosk.feature.auth.data.source.remote;

import com.fmv.healthkiosk.feature.auth.data.source.remote.model.AuthResponse;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.LoginRequest;

import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthService {

    @POST("login")
    Single<AuthResponse> loginPhoneNumber(@Body LoginRequest loginRequest);

    @Multipart
    @POST("users/add")
    Single<AuthResponse> register(
            @Part("name") RequestBody name,
            @Part("address") RequestBody address,
            @Part("gender") RequestBody gender,
            @Part("age") RequestBody age,
            @Part("dob") RequestBody dob,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("email") RequestBody email,
            @Part MultipartBody.Part image
    );

    @Multipart
    @PUT("users/{id}")
    Single<AuthResponse> update(
            @Path("id") Integer userId,  // userId will be replaced dynamically in the URL
            @Part("name") RequestBody name,
            @Part("gender") RequestBody gender,
            @Part("email") RequestBody email,
            @Part("phoneNumber") RequestBody phoneNumber,
            @Part("dob") RequestBody dob
    );

}
