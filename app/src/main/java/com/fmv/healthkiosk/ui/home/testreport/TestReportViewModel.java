package com.fmv.healthkiosk.ui.home.testreport;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestResultUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class TestReportViewModel extends BaseViewModel {

    MedicalPackage medicalPackage;
    TestItemList testItemList;

    private final TestResultUseCase testResultUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<TestResult>> inflatedTestResult = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TestReportViewModel(SavedStateHandle savedStateHandle, TestResultUseCase testResultUseCase) {
        super(savedStateHandle);
        this.medicalPackage = getArgument("medicalPackage");
        this.testItemList = getArgument("testItemList");

        this.testResultUseCase = testResultUseCase;
        inflatedTestResult.setValue(testResultUseCase.mapToTestResults(testItemList.getTestItemList()));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
