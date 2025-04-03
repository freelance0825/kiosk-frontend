package com.fmv.healthkiosk.ui.home.medicalpackage;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MedicalPackageViewModel extends BaseViewModel {

    private final MedicalPackageUseCase medicalPackageUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<MedicalPackage>> medicalPackageList = new MutableLiveData<>();


    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MedicalPackageViewModel(SavedStateHandle savedStateHandle, MedicalPackageUseCase medicalPackageUseCase) {
        super(savedStateHandle);
        this.medicalPackageUseCase = medicalPackageUseCase;

        getMedicalPackages();
    }

    public void getMedicalPackages() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                medicalPackageUseCase.getMedicalPackages()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                medicalPackages -> {
                                    medicalPackageList.setValue(medicalPackages);
                                },
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
