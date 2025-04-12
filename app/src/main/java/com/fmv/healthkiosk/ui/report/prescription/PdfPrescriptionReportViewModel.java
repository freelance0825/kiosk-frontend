package com.fmv.healthkiosk.ui.report.prescription;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class PdfPrescriptionReportViewModel extends BaseViewModel {

    final MutableLiveData<AppointmentModel> appointmentModel = new MutableLiveData<>(null);

    private final CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public PdfPrescriptionReportViewModel(
            SavedStateHandle savedStateHandle
    ) {
        super(savedStateHandle);
    }

    public void setData(AppointmentModel appointmentModel) {
        this.appointmentModel.setValue(appointmentModel);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
