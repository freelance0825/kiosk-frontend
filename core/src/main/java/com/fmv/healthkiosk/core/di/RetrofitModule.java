package com.fmv.healthkiosk.core.di;


import com.fmv.healthkiosk.core.base.data.BaseRetrofit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    @Provides
    @Singleton
    public static Retrofit provideRetrofit() {
        return BaseRetrofit.createRetrofit();
    }
}

