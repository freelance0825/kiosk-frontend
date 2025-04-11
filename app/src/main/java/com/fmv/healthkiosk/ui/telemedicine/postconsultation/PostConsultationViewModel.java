package com.fmv.healthkiosk.ui.telemedicine.postconsultation;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorForNotification;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class PostConsultationViewModel extends BaseViewModel {

    final DoctorForNotification doctorForNotification;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<DoctorForNotification>> doctorList = new MutableLiveData<>();



    final MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    final MutableLiveData<String> selectedTime = new MutableLiveData<>("");


    final MutableLiveData<Boolean> isAppointmentSubmitted = new MutableLiveData<>(false);

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public PostConsultationViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
        this.doctorForNotification = getArgument("doctor");

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
