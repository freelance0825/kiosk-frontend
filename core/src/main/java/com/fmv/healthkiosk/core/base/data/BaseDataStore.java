package com.fmv.healthkiosk.core.base.data;

import android.content.Context;

import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;

import com.fmv.healthkiosk.core.BuildConfig;

import io.reactivex.Completable;
import io.reactivex.Single;

public abstract class BaseDataStore {

    protected final RxDataStore<Preferences> dataStore;

    protected BaseDataStore(Context context) {
        String STORE_NAME = BuildConfig.APP_DATASTORE_NAME;
        this.dataStore = new RxPreferenceDataStoreBuilder(context, STORE_NAME).build();
    }

    protected <T> Single<T> getValue(Preferences.Key<T> key, T defaultValue) {
        return dataStore.data()
                .map(preferences -> preferences.get(key) != null ? preferences.get(key) : defaultValue)
                .firstOrError();
    }

    protected <T> Completable setValue(Preferences.Key<T> key, T value) {
        return dataStore.updateDataAsync(prefs -> {
            MutablePreferences mutablePreferences = prefs.toMutablePreferences();
            mutablePreferences.set(key, value);
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }

    protected Completable clearAll() {
        return dataStore.updateDataAsync(prefs -> {
            MutablePreferences mutablePreferences = prefs.toMutablePreferences();
            mutablePreferences.clear();
            return Single.just(mutablePreferences);
        }).ignoreElement();
    }
}
