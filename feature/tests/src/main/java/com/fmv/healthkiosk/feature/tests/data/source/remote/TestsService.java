package com.fmv.healthkiosk.feature.tests.data.source.remote;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.MedicalPackageResponse;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.MedicalPackageResponseItem;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface TestsService {

    @GET("package")
    Single<List<MedicalPackageResponseItem>> getMedicalPackages();

//    @POST("login")
//    Single<AuthResponse> loginPhoneNumber(@Body LoginRequest loginRequest);
//
//    @Multipart
//    @POST("users/add")
//    Single<AuthResponse> register(
//            @Part("name") RequestBody name,
//            @Part("address") RequestBody address,
//            @Part("gender") RequestBody gender,
//            @Part("age") RequestBody age,
//            @Part("dob") RequestBody dob,
//            @Part("phoneNumber") RequestBody phoneNumber,
//            @Part MultipartBody.Part image
//    );
}
