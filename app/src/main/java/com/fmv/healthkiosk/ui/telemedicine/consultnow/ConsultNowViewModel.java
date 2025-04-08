package com.fmv.healthkiosk.ui.telemedicine.consultnow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ConsultNowViewModel extends BaseViewModel {

    private final GetMyAppointmentsUseCase getMyAppointmentsUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<Doctor>> doctorList = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ConsultNowViewModel(SavedStateHandle savedStateHandle, GetMyAppointmentsUseCase getMyAppointmentsUseCase) {
        super(savedStateHandle);
        this.getMyAppointmentsUseCase = getMyAppointmentsUseCase;

        getMyAppointments();
    }

    public void getMyAppointments() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getMyAppointmentsUseCase.execute()
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
