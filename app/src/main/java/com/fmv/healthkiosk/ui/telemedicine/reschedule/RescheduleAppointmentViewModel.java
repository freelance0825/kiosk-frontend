package com.fmv.healthkiosk.ui.telemedicine.reschedule;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class RescheduleAppointmentViewModel extends BaseViewModel {

    private final UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase;

    final int appointmentId;
    final DoctorModel doctorModel;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<AppointmentResponse> updatedAppointments = new MutableLiveData<>();

    final MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    final MutableLiveData<String> selectedTime = new MutableLiveData<>("");
    final MutableLiveData<Boolean> isAppointmentSubmitted = new MutableLiveData<>(false);

    private final CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public RescheduleAppointmentViewModel(SavedStateHandle savedStateHandle, UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase) {
        super(savedStateHandle);
        this.updateMyApppointmentsUseCase = updateMyApppointmentsUseCase;
        this.doctorModel = getArgument("doctor");

        Integer id = savedStateHandle.get("appointmentId");
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID is missing in navigation arguments.");
        }
        this.appointmentId = id;
        Log.d("RescheduleAppointmentViewModel", "Appointment ID in ViewModel: " + appointmentId);
    }

    public void updateMyAppointments(String dateTime) {
        Log.d("RescheduleAppointmentViewModel", "Updating appointment ID: " + appointmentId);
        isLoading.setValue(true);

        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setDateTime(dateTime);

        disposables.add(
                updateMyApppointmentsUseCase.execute(appointmentId, appointmentRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                updatedAppointments::setValue,
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        )
        );
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
