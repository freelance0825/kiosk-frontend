package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetPostConsultationUseCase;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class PostConsultationViewModel extends BaseViewModel {

    private final GetPostConsultationUseCase getPostConsultationUseCase;

    final int appointmentId;
    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<AppointmentModel> appointment = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public PostConsultationViewModel(SavedStateHandle savedStateHandle, GetPostConsultationUseCase getPostConsultationUseCase) {
        super(savedStateHandle);
        this.getPostConsultationUseCase = getPostConsultationUseCase;

        // Retrieve the doctorId from SavedStateHandle
        Integer id = savedStateHandle.get("appointmentId");
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID is missing in navigation arguments.");
        }

        // Initialize the doctorId
        this.appointmentId = id;
        Log.d("PostConsultationViewModel", "Appointment ID in ViewModel: " + appointmentId);

        // Fetch appointments for the doctorId
        getAppointment();
    }

    public void getAppointment() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getPostConsultationUseCase.execute(appointmentId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                appointment::setValue,
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
