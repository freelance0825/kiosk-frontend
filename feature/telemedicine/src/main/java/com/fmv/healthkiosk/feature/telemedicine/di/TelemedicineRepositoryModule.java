package com.fmv.healthkiosk.feature.telemedicine.di;

import android.content.Context;

import com.fmv.healthkiosk.feature.telemedicine.data.repo.TelemedicineRepositoryImpl;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.TelemedicineService;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.utils.AppointmentDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class TelemedicineRepositoryModule {

    @Provides
    @Singleton
    TelemedicineRepository provideTelemedicineRepository(@ApplicationContext Context context, TelemedicineService telemedicineService) {
        return new TelemedicineRepositoryImpl(context, telemedicineService);
    }

    @Provides
    @Singleton
    TelemedicineService provideTelemedicineService(@GsonRetrofit Retrofit retrofit) {
        return retrofit.create(TelemedicineService.class);
    }

    // Provide Gson instance
    @Provides
    @Singleton
    public static Gson provideGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Appointment.class, new AppointmentDeserializer())
                .create();
    }

    // This method provides a Retrofit instance with a Gson converter
    @Provides
    @Singleton
    @GsonRetrofit
    Retrofit provideGsonRetrofit(Gson gson) {
        return new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/")  // Correct base URL
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // Add RxJava2 adapter
                .build();
    }


}
