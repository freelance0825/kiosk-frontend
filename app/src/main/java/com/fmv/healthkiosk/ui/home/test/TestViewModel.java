package com.fmv.healthkiosk.ui.home.test;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class TestViewModel extends BaseViewModel {

    public TestItemList testItemListPackage;
    public MedicalPackage medicalPackage;


    public final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public final MutableLiveData<List<TestItem>> testItemList = new MutableLiveData<>();
    public final MutableLiveData<TestItem> selectedTestItem = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public TestViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
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
