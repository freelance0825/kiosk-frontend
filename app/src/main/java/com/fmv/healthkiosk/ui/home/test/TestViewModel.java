package com.fmv.healthkiosk.ui.home.test;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;
import com.fmv.healthkiosk.feature.tests.domain.usecase.PostTestHistoryUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestResultUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import org.threeten.bp.Instant;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class TestViewModel extends BaseViewModel {

    public TestItemList testItemListPackage;
    public MedicalPackage medicalPackage;

    private final TestResultUseCase testResultUseCase;
    private final PostTestHistoryUseCase postTestHistoryUseCase;
    private final AccountUseCase accountUseCase;

    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<TestHistoryModel> testHistoryModel = new MutableLiveData<>(null);
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public final MutableLiveData<List<TestItem>> testItemList = new MutableLiveData<>();
    public final MutableLiveData<TestItem> selectedTestItem = new MutableLiveData<>();


    final MutableLiveData<String> username = new MutableLiveData<>();
    final MutableLiveData<String> dateOfBirth = new MutableLiveData<>();
    final MutableLiveData<String> gender = new MutableLiveData<>();
    final MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    final MutableLiveData<Integer> age = new MutableLiveData<>();
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TestViewModel(SavedStateHandle savedStateHandle, TestResultUseCase testResultUseCase, AccountUseCase accountUseCase, PostTestHistoryUseCase postTestHistoryUseCase) {
        super(savedStateHandle);
        this.testResultUseCase = testResultUseCase;
        this.postTestHistoryUseCase = postTestHistoryUseCase;
        this.accountUseCase = accountUseCase;
    }

    public void postTestHistory(List<TestItem> testItemsList) {
        List<TestsResultModel> testsResultModels = testResultUseCase.mapToTestResults(testItemsList);
        int userId = accountUseCase.getUserID().blockingFirst();

        TestHistoryModel testHistoryModelRequest = new TestHistoryModel();

        testHistoryModelRequest.setPackageName((medicalPackage != null && medicalPackage.getName() != null) ? medicalPackage.getName() : null);
        testHistoryModelRequest.setUserId(userId);
        testHistoryModelRequest.setTests(testsResultModels);

        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                postTestHistoryUseCase.postTestHistory(testHistoryModelRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                this.testHistoryModel::setValue,
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

    public void selectTestItem(TestItem testItem) {
        List<TestItem> currentList = testItemList.getValue();
        if (currentList == null) return;

        TestItem currentSelectedItem = selectedTestItem.getValue();
        List<TestItem> updatedList = new ArrayList<>();

        for (TestItem item : currentList) {
            boolean isSelected = item.getId().equals(testItem.getId());
            boolean isCurrentSelected = currentSelectedItem != null && item.getId().equals(currentSelectedItem.getId());

            if (isCurrentSelected && currentSelectedItem.isTested() < 2) {
                item = new TestItem(item.getId(), item.getName(), null, item.getIcon(), 0, false, item.getTestExtension(), item.isGeneral()); // Reset test state
            } else {
                item = new TestItem(item.getId(), item.getName(), item.getTestResult(), item.getIcon(), item.isTested(), isSelected, item.getTestExtension(), item.isGeneral());
            }

            updatedList.add(item);
        }

        testItemList.setValue(updatedList);
        selectedTestItem.setValue(updatedList.stream()
                .filter(item -> item.getId().equals(testItem.getId()))
                .findFirst()
                .orElse(null));
    }

    public void updateTestItem(String id, int isTestedValue, String testResult) {
        List<TestItem> currentList = testItemList.getValue();
        if (currentList == null) return;

        List<TestItem> updatedList = new ArrayList<>();
        TestItem updatedItem = null;

        for (TestItem item : currentList) {
            if (item.getId().equals(id)) {
                updatedItem = new TestItem(
                        item.getId(),
                        item.getName(),
                        testResult,
                        item.getIcon(),
                        isTestedValue,
                        item.isActive(),
                        item.getTestExtension(),
                        item.isGeneral()
                );
                updatedList.add(updatedItem);
            } else {
                updatedList.add(item);
            }
        }

        testItemList.setValue(updatedList);
        if (updatedItem != null) selectedTestItem.setValue(updatedItem);
    }

    public void updateTestExtension(String id, String testExtension) {
        List<TestItem> currentList = testItemList.getValue();
        if (currentList == null) return;

        List<TestItem> updatedList = new ArrayList<>();
        TestItem updatedItem = null;

        for (TestItem item : currentList) {
            if (item.getId().equals(id)) {
                updatedItem = new TestItem(
                        item.getId(),
                        item.getName(),
                        item.getTestResult(),
                        item.getIcon(),
                        item.isTested(),
                        item.isActive(),
                        testExtension,
                        item.isGeneral()
                );
                updatedList.add(updatedItem);
            } else {
                updatedList.add(item);
            }
        }

        testItemList.setValue(updatedList);
        if (updatedItem != null) selectedTestItem.setValue(updatedItem);
    }

    public List<TestItem> getSelectedTestItems() {
        List<TestItem> currentList = testItemList.getValue();
        if (currentList == null) return new ArrayList<>();
        List<TestItem> updatedList = new ArrayList<>();

        if (medicalPackage != null) {
            for (TestItem item : currentList) {
                item.setActive(false);
                updatedList.add(item);
            }
        } else {
            for (TestItem item : currentList) {
                if (item.isActive()) {
                    item.setActive(false);
                    updatedList.add(item);
                }
            }
        }

        return updatedList;
    }
}
