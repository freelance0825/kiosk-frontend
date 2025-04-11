package com.fmv.healthkiosk.ui.telemedicine.bookappointment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAvailableDoctorsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class BookAppointmentViewModel extends BaseViewModel {

    private final GetAvailableDoctorsUseCase getAvailableDoctorsUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<Doctor>> doctorList = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public BookAppointmentViewModel(SavedStateHandle savedStateHandle, GetAvailableDoctorsUseCase getAvailableDoctorsUseCase) {
        super(savedStateHandle);
        this.getAvailableDoctorsUseCase = getAvailableDoctorsUseCase;

        getAvailableDoctors();
    }

    public void getAvailableDoctors() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getAvailableDoctorsUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                doctorList::setValue,
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
