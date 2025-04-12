package com.fmv.healthkiosk.ui.telemedicine.myappointment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;
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

    private final CancelMyAppointmentsUseCase cancelMyAppointmentsUseCase;

    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<AppointmentModel>> myAppointmentList = new MutableLiveData<>();

    final MutableLiveData<Integer> selectedAppointmentToCancel = new MutableLiveData<>(null);

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyAppointmentViewModel(SavedStateHandle savedStateHandle, GetMyAppointmentsUseCase getMyAppointmentsUseCase,
                                  AccountUseCase accountUseCase, CancelMyAppointmentsUseCase cancelMyAppointmentsUseCase) {
        super(savedStateHandle);
        this.getMyAppointmentsUseCase = getMyAppointmentsUseCase;
        this.accountUseCase = accountUseCase;
        this.cancelMyAppointmentsUseCase = cancelMyAppointmentsUseCase;

        // Fetch appointments on initialization
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

    public void cancelAppointment(int appointmentId) {
        disposables.add(
                cancelMyAppointmentsUseCase.execute(appointmentId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(throwable -> {
                                    errorMessage.setValue("Failed to cancel appointment. Please try again later.");
                                }
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
