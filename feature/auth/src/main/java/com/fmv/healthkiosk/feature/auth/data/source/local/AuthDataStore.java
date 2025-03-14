package com.fmv.healthkiosk.feature.auth.data.source.local;

import android.content.Context;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;

import com.fmv.healthkiosk.core.BuildConfig;
import com.fmv.healthkiosk.core.base.data.BaseDataStore;

import io.reactivex.Completable;
import io.reactivex.Observable;
public class AuthDataStore extends BaseDataStore {

    private static final Preferences.Key<String> USERNAME = PreferencesKeys.stringKey("name_preferences");
    private static final Preferences.Key<String> DATE_OF_BIRTH = PreferencesKeys.stringKey("date_of_birth_preferences");
    private static final Preferences.Key<String> GENDER = PreferencesKeys.stringKey("gender_preferences");
    private static final Preferences.Key<String> PHONE_NUMBER = PreferencesKeys.stringKey("phone_number_preferences");
    private static final Preferences.Key<Integer> AGE = PreferencesKeys.intKey("age_preferences");
    private static final Preferences.Key<Boolean> IS_LOGGED_IN = PreferencesKeys.booleanKey("is_logged_in");

    public AuthDataStore(Context context) {
        super(context);
    }

    public Observable<String> getUsername() {
        return observeValue(USERNAME, "");
    }

    public Completable setUsername(String username) {
        return setValue(USERNAME, username);
    }

    public Observable<String> getDateOfBirth() {
        return observeValue(DATE_OF_BIRTH, "");
    }

    public Completable setDateOfBirth(String dob) {
        return setValue(DATE_OF_BIRTH, dob);
    }

    public Observable<String> getGender() {
        return observeValue(GENDER, "");
    }

    public Completable setGender(String gender) {
        return setValue(GENDER, gender);
    }

    public Observable<String> getPhoneNumber() {
        return observeValue(PHONE_NUMBER, "");
    }

    public Completable setPhoneNumber(String phoneNumber) {
        return setValue(PHONE_NUMBER, phoneNumber);
    }

    public Observable<Integer> getAge() {
        return observeValue(AGE, 0);
    }

    public Completable setAge(int age) {
        return setValue(AGE, age);
    }

    public Observable<Boolean> isLoggedIn() {
        return observeValue(IS_LOGGED_IN, false);
    }

    public Completable setLoggedIn(boolean isLoggedIn) {
        return setValue(IS_LOGGED_IN, isLoggedIn);
    }

    public Completable clearData() {
        return clearAll();
    }
}
