package com.fmv.healthkiosk.ui.telemedicine.myappointment;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MyAppointmentViewModel extends BaseViewModel {

    private final GetMyAppointmentsUseCase getMyAppointmentsUseCase;
    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<AppointmentModel>> myAppointmentList = new MutableLiveData<>();

    final MutableLiveData<AppointmentModel> selectedAppointmentToCancel = new MutableLiveData<>(null);

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyAppointmentViewModel(SavedStateHandle savedStateHandle, GetMyAppointmentsUseCase getMyAppointmentsUseCase, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.getMyAppointmentsUseCase = getMyAppointmentsUseCase;
        this.accountUseCase = accountUseCase;

        getMyAppointments();
    }

    public void getMyAppointments() {
        int userId = accountUseCase.getUserID().blockingFirst();

        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getMyAppointmentsUseCase.execute(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                myAppointmentList::setValue,
                                throwable -> {
                                    errorMessage.setValue(throwable.getMessage());
                                }
                        ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
