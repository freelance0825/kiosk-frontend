package com.fmv.healthkiosk.ui.home.testreporthistory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.usecase.GetUserTestHistoryUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class TestReportHistoryViewModel extends BaseViewModel {

    private final GetUserTestHistoryUseCase getUserTestHistoryUseCase;
    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<TestHistoryModel>> myReportList = new MutableLiveData<>();

    // PAGINATION CONFIG
    final MutableLiveData<List<TestHistoryModel>> pagedReportItems = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextReportButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackReportButton = new MutableLiveData<>(false);

    private final int REPORT_PAGE_SIZE = 4;
    private int currentPageIndex = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TestReportHistoryViewModel(SavedStateHandle savedStateHandle, GetUserTestHistoryUseCase getUserTestHistoryUseCase, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.getUserTestHistoryUseCase = getUserTestHistoryUseCase;
        this.accountUseCase = accountUseCase;

        getMyReports();
    }

    public void getMyReports() {
        int userId = accountUseCase.getUserID().blockingFirst();

        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getUserTestHistoryUseCase.getUserTestHistory(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                success -> {
                                    myReportList.setValue(success);
                                    loadCurrentPage();
                                },
                                throwable -> {
                                    errorMessage.setValue(throwable.getMessage());
                                }
                        ));
    }

    private void loadCurrentPage() {
        int fromIndex = currentPageIndex * REPORT_PAGE_SIZE;
        int toIndex = Math.min(fromIndex + REPORT_PAGE_SIZE, Objects.requireNonNull(myReportList.getValue()).size());

        List<TestHistoryModel> pageItems = new ArrayList<>(myReportList.getValue().subList(fromIndex, toIndex));
        while (pageItems.size() < REPORT_PAGE_SIZE) {
            pageItems.add(null);
        }

        pagedReportItems.setValue(pageItems);

        int maxPage = (int) Math.ceil((double) myReportList.getValue().size() / REPORT_PAGE_SIZE);
        showBackReportButton.setValue(currentPageIndex > 0);
        showNextReportButton.setValue(currentPageIndex < maxPage - 1);
    }

    public void nextReportPage() {
        int maxPage = (int) Math.ceil((double) myReportList.getValue().size() / REPORT_PAGE_SIZE);
        if (currentPageIndex < maxPage - 1) {
            currentPageIndex++;
            loadCurrentPage();
        }
    }

    public void previousReportPage() {
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
