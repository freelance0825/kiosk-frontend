package com.fmv.healthkiosk.feature.tests.data.source.remote;

import com.fmv.healthkiosk.feature.tests.data.source.remote.model.MedicalPackageResponse;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.MedicalPackageResponseItem;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryRequest;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.TestHistoryResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TestsService {

    @Headers("Content-Type: application/json")
    @GET("medical/package")
    Single<List<MedicalPackageResponseItem>> getMedicalPackages();

    @POST("medical/package/history")
    Single<TestHistoryResponse> postMedicalTestHistory(
            @Body TestHistoryRequest testHistoryRequest
    );

    @POST("custom/package/history")
    Single<TestHistoryResponse> postCustomTestHistory(
            @Body TestHistoryRequest testHistoryRequest
    );

    @Headers("Content-Type: application/json")
    @GET("test/history/{userId}")
    Single<List<TestHistoryResponse>> getUserTestHistory(
            @Path("userId") int userId
    );
}
