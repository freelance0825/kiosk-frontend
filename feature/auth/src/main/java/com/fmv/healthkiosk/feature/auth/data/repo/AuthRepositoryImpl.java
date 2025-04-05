package com.fmv.healthkiosk.feature.auth.data.repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.fmv.healthkiosk.feature.auth.R;
import com.fmv.healthkiosk.feature.auth.data.source.local.AuthDataStore;
import com.fmv.healthkiosk.feature.auth.data.source.remote.AuthService;
import com.fmv.healthkiosk.feature.auth.data.source.remote.model.LoginRequest;
import com.fmv.healthkiosk.feature.auth.domain.repo.AuthRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class AuthRepositoryImpl implements AuthRepository {

    private final Context context;
    private final AuthService authService;
    private final AuthDataStore authDataStore;

    @Inject
    public AuthRepositoryImpl(Context context, AuthService authService, AuthDataStore authDataStore) {
        this.context = context;
        this.authService = authService;
        this.authDataStore = authDataStore;
    }

    @Override
    public Single<String> login(String phoneNumber) {
        return authService.loginPhoneNumber(new LoginRequest(phoneNumber, "", ""))
                .flatMap(authResponse ->
                        Completable.concatArray(
                        authDataStore.setPhoneNumber(phoneNumber),
                        authDataStore.setUsername(authResponse.getName()),
                        authDataStore.setDateOfBirth(authResponse.getDateOfBirth()),
                        authDataStore.setGender(authResponse.getGender()),
                        authDataStore.setAge(Integer.parseInt(authResponse.getAge()))
                ).toSingleDefault("Login successful!"));
    }

    @Override
    public Single<String> registerUser(String name, String gender, String age, String dob, String phoneNumber) {
        RequestBody namePart = RequestBody.create(name, MediaType.parse("text/plain"));
        RequestBody addressPart = RequestBody.create("address", MediaType.parse("text/plain"));
        RequestBody genderPart = RequestBody.create(gender, MediaType.parse("text/plain"));
        RequestBody agePart = RequestBody.create(age, MediaType.parse("text/plain"));
        RequestBody dobPart = RequestBody.create(dob, MediaType.parse("text/plain"));
        RequestBody phoneNumberPart = RequestBody.create(phoneNumber, MediaType.parse("text/plain"));

        File imageFile;
        try {
            imageFile = getFileFromDrawable(context, R.drawable.img);
        } catch (IOException e) {
            return Single.error(e);
        }

        RequestBody requestFile = RequestBody.create(imageFile, MediaType.parse("image/*"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestFile);

        return authService.register(namePart, addressPart, genderPart, agePart, dobPart, phoneNumberPart, imagePart)
                .flatMap(authResponse -> Completable.concatArray(
                        authDataStore.setPhoneNumber(phoneNumber),
                        authDataStore.setUsername(authResponse.getName()),
                        authDataStore.setDateOfBirth(authResponse.getDateOfBirth()),
                        authDataStore.setGender(authResponse.getGender()),
                        authDataStore.setAge(Integer.parseInt(authResponse.getAge()))
                ).toSingleDefault("Register successful!"));
    }

    @Override
    public Observable<String> getUsername() {
        return authDataStore.getUsername();
    }

    @Override
    public Observable<String> getDateOfBirth() {
        return authDataStore.getDateOfBirth();
    }

    @Override
    public Observable<String> getGender() {
        return authDataStore.getGender();
    }

    @Override
    public Observable<String> getPhoneNumber() {
        return authDataStore.getPhoneNumber();
    }

    @Override
    public Observable<Integer> getAge() {
        return authDataStore.getAge();
    }

    @Override
    public Observable<Boolean> isLoggedIn() {
        return authDataStore.isLoggedIn();
    }

    @Override
    public Completable setIsLoggedIn(boolean isLoggedIn) {
        return authDataStore.setLoggedIn(isLoggedIn);
    }

    @Override
    public Completable logout() {
        return authDataStore.clearData();
    }

    private File getFileFromDrawable(Context context, int drawableId) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawableId);
        File file = new File(context.getCacheDir(), "temp_image.jpg");

        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
        }
        return file;
    }
}
