package com.fmv.healthkiosk.ui.home.testreport;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestResultUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class TestReportViewModel extends BaseViewModel {

//    MedicalPackage medicalPackage;
    TestHistoryModel testHistoryModel;

    private final TestResultUseCase testResultUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // PAGINATION CONFIG
    final MutableLiveData<List<TestsResultModel>> pagedTestResult = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextTestResultButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackTestResultButton = new MutableLiveData<>(false);


    final List<TestsResultModel> inflatedTestResult = new ArrayList<>();
    private final int TEST_RESULT_PAGE_SIZE = 4;
    private int currentPageIndex = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TestReportViewModel(SavedStateHandle savedStateHandle, TestResultUseCase testResultUseCase) {
        super(savedStateHandle);
        this.testHistoryModel = getArgument("testHistoryModel");

        this.testResultUseCase = testResultUseCase;

        inflatedTestResult.addAll(testHistoryModel.getTests());
        loadCurrentPage();
    }

    private void loadCurrentPage() {
        int fromIndex = currentPageIndex * TEST_RESULT_PAGE_SIZE;
        int toIndex = Math.min(fromIndex + TEST_RESULT_PAGE_SIZE, inflatedTestResult.size());

        List<TestsResultModel> pageItems = new ArrayList<>(inflatedTestResult.subList(fromIndex, toIndex));
        while (pageItems.size() < TEST_RESULT_PAGE_SIZE) {
            pageItems.add(null);
        }

        pagedTestResult.setValue(pageItems);

        int maxPage = (int) Math.ceil((double) inflatedTestResult.size() / TEST_RESULT_PAGE_SIZE);
        showBackTestResultButton.setValue(currentPageIndex > 0);
        showNextTestResultButton.setValue(currentPageIndex < maxPage - 1);
    }

    public void nextTestResultPage() {
        int maxPage = (int) Math.ceil((double) inflatedTestResult.size() / TEST_RESULT_PAGE_SIZE);
        if (currentPageIndex < maxPage - 1) {
            currentPageIndex++;
            loadCurrentPage();
        }
    }

    public void previousTestResultPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            loadCurrentPage();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
