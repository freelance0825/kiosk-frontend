package com.fmv.healthkiosk.feature.auth.di;

import android.content.Context;
import com.fmv.healthkiosk.feature.auth.data.source.local.AuthDataStore;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

import javax.inject.Singleton;

@Module
@InstallIn(SingletonComponent.class)
public class AuthDataStoreModule {

    @Provides
    @Singleton
    public static AuthDataStore provideUserPreferencesDataStore(Context context) {
        return new AuthDataStore(context);
    }
}

