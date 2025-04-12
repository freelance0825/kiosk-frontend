package com.fmv.healthkiosk.ui.report.testing;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class PdfTestingReportViewModel extends BaseViewModel {

    private final AccountUseCase accountUseCase;

    final MutableLiveData<MedicalPackage> medicalPackage = new MutableLiveData<>(null);
    final MutableLiveData<ArrayList<TestResult>> generalTestList = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<ArrayList<TestResult>> advancedTestList = new MutableLiveData<>(new ArrayList<>());


    final MutableLiveData<String> username = new MutableLiveData<>();
    final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    final MutableLiveData<String> gender = new MutableLiveData<>();
    final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    final MutableLiveData<Integer> age = new MutableLiveData<>();
    final MutableLiveData<Integer> userId = new MutableLiveData<>();


    private final CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public PdfTestingReportViewModel(
            SavedStateHandle savedStateHandle, AccountUseCase accountUseCase
    ) {
        super(savedStateHandle);
        this.accountUseCase = accountUseCase;

        observeProfileData();
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.getUserID()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId::setValue, throwable -> userId.setValue(0)));

        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(username::setValue, throwable -> username.setValue("Unknown")));

        disposables.add(accountUseCase.getDateOfBirth()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dateOfBirth::setValue, throwable -> dateOfBirth.setValue("N/A")));

        disposables.add(accountUseCase.getGender()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gender::setValue, throwable -> gender.setValue("Unknown")));

        disposables.add(accountUseCase.getPhoneNumber()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(phoneNumber::setValue, throwable -> phoneNumber.setValue("Unknown")));

        disposables.add(accountUseCase.getAge()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(age::setValue, throwable -> age.setValue(0)));
    }

    public void setData(ArrayList<TestResult> testingResults, MedicalPackage medicalPackage) {
        this.medicalPackage.setValue(medicalPackage);
        mapTestResultsToLists(testingResults);
    }

    private void mapTestResultsToLists(ArrayList<TestResult> testingResults) {
        ArrayList<TestResult> generalTest = new ArrayList<>();
        ArrayList<TestResult> advancedTest = new ArrayList<>();

        for (TestResult testResult : testingResults) {
            if (testResult.getIsGeneralTest()) {
                generalTest.add(testResult);
            } else {
                advancedTest.add(testResult);
            }
        }

        this.generalTestList.setValue(generalTest);
        this.advancedTestList.setValue(advancedTest);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
