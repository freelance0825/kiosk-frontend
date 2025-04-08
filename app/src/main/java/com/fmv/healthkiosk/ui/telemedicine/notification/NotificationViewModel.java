package com.fmv.healthkiosk.ui.telemedicine.notification;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAllNotificationsUseCase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class NotificationViewModel extends BaseViewModel {

    private final GetAllNotificationsUseCase getAllNotificationsUseCase;
    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<Appointment>> todayNotificationList = new MutableLiveData<>();
    final MutableLiveData<List<Appointment>> allNotificationList = new MutableLiveData<>();

    final MutableLiveData<String> username = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public NotificationViewModel(SavedStateHandle savedStateHandle, GetAllNotificationsUseCase getAllNotificationsUseCase, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.getAllNotificationsUseCase = getAllNotificationsUseCase;
        this.accountUseCase = accountUseCase;

        getAllNotifications();
        observeProfileData();
    }

    public void getAllNotifications() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getAllNotificationsUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                notifications -> {
                                    List<Appointment> todayList = new ArrayList<>();
                                    List<Appointment> allList = new ArrayList<>();

                                    for (Appointment notif : notifications) {
                                        allList.add(notif);

                                        if (isToday(notif.getDateTime())) {
                                            todayList.add(notif);
                                        }
                                    }

                                    todayNotificationList.setValue(todayList);
                                    allNotificationList.setValue(allList);
                                },
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        )
        );
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(username::setValue, throwable -> username.setValue("Unknown")));
    }

    private boolean isToday(long timestamp) {
        Calendar notifCal = Calendar.getInstance();
        notifCal.setTimeInMillis(timestamp);

        Calendar todayCal = Calendar.getInstance();

        return notifCal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR)
                && notifCal.get(Calendar.DAY_OF_YEAR) == todayCal.get(Calendar.DAY_OF_YEAR);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
