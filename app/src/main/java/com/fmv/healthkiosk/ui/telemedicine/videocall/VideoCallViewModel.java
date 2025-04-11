package com.fmv.healthkiosk.ui.telemedicine.videocall;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorForNotification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class VideoCallViewModel extends BaseViewModel {

    private final AccountUseCase accountUseCase;

    final DoctorModel doctorForNotification;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    final MutableLiveData<String> username = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    final MutableLiveData<String> timeString = new MutableLiveData<>();
    private long elapsedSeconds = 0;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable timerRunnable;

    @Inject
    public VideoCallViewModel(SavedStateHandle savedStateHandle, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.accountUseCase = accountUseCase;
        this.doctorForNotification = getArgument("doctorForNotification");

        observeProfileData();
        startTimer();
    }


    private void observeProfileData() {
        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(username::setValue, throwable -> username.setValue("Unknown")));
    }

    public void startTimer() {
        stopTimer();

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                elapsedSeconds++;
                timeString.setValue(formatTime(elapsedSeconds));
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(timerRunnable);
    }

    public void stopTimer() {
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
        elapsedSeconds = 0;
        timeString.setValue(formatTime(elapsedSeconds));
    }

    public void pauseTimer() {
        if (handler != null && timerRunnable != null) {
            handler.removeCallbacks(timerRunnable);
        }
    }

    private String formatTime(long seconds) {
        long hrs = seconds / 3600;
        long mins = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hrs > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d:%02d", hrs, mins, secs);
        } else {
            return String.format(Locale.getDefault(), "%02d:%02d", mins, secs);
        }
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        stopTimer();
        disposables.clear();
    }
}
