package com.fmv.healthkiosk.ui.report.testing;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class PdfTestingReportViewModel extends BaseViewModel {

    final MutableLiveData<TestHistoryModel> testHistoryModel = new MutableLiveData<>(null);
    final MutableLiveData<ArrayList<TestsResultModel>> generalTestList = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<ArrayList<TestsResultModel>> advancedTestList = new MutableLiveData<>(new ArrayList<>());


    private final CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public PdfTestingReportViewModel(
            SavedStateHandle savedStateHandle
    ) {
        super(savedStateHandle);
    }


    public void setData(TestHistoryModel testHistoryModel) {
        this.testHistoryModel.setValue(testHistoryModel);
        mapTestResultsToLists(testHistoryModel.getTests());
    }

    private void mapTestResultsToLists(List<TestsResultModel> testingResults) {
        ArrayList<TestsResultModel> generalTest = new ArrayList<>();
        ArrayList<TestsResultModel> advancedTest = new ArrayList<>();

        for (TestsResultModel testResult : testingResults) {
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
