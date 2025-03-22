package com.fmv.healthkiosk.ui.home.customizetest;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.disposables.CompositeDisposable;

@HiltViewModel
public class CustomizeTestViewModel extends BaseViewModel {

    MedicalPackage medicalPackage;

    private final TestsPresetUseCase testsPresetUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<TestItem>> testItemListGeneral = new MutableLiveData<>();
    final MutableLiveData<List<TestItem>> testItemListAdvanced = new MutableLiveData<>();


    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public CustomizeTestViewModel(SavedStateHandle savedStateHandle, TestsPresetUseCase testsPresetUseCase) {
        super(savedStateHandle);
        this.medicalPackage = getArgument("medicalPackage");

        this.testsPresetUseCase = testsPresetUseCase;

        if (medicalPackage == null) {
            getTestsPreset(null);
        } else {
            getTestsPreset(medicalPackage.getTestPresets());
        }
    }

    public void getTestsPreset(String testPresets) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        List<TestItem> testItems = testsPresetUseCase.getTestItems(testPresets);

        List<TestItem> generalItems = testItems.stream()
                .filter(TestItem::isGeneral)
                .collect(Collectors.toList());

        List<TestItem> advancedItems = testItems.stream()
                .filter(item -> !item.isGeneral())
                .collect(Collectors.toList());

        testItemListGeneral.setValue(generalItems);
        testItemListAdvanced.setValue(advancedItems);
    }

    public void toggleTestItemGeneral(TestItem testItem) {
        List<TestItem> currentList = testItemListGeneral.getValue();

        if (currentList == null) return;

        List<TestItem> updatedList = new ArrayList<>();
        for (TestItem item : currentList) {
            if (item.getId().equals(testItem.getId())) {
                item.setActive(!item.isActive());
            }
            updatedList.add(item);
        }

        testItemListGeneral.setValue(updatedList);
    }

    public void toggleTestItemAdvanced(TestItem testItem) {
        List<TestItem> currentList = testItemListAdvanced.getValue();

        if (currentList == null) return;

        List<TestItem> updatedList = new ArrayList<>();
        for (TestItem item : currentList) {
            if (item.getId().equals(testItem.getId())) {
                item.setActive(!item.isActive());
            }
            updatedList.add(item);
        }

        testItemListAdvanced.setValue(updatedList);
    }


    public List<TestItem> getSelectedTestItems() {
        List<TestItem> generalList = testItemListGeneral.getValue();
        List<TestItem> advancedList = testItemListAdvanced.getValue();

        List<TestItem> selectedItems = new ArrayList<>();

        if (medicalPackage != null) {
            if (generalList != null) selectedItems.addAll(generalList);
            if (advancedList != null) selectedItems.addAll(advancedList);
        } else {
            if (generalList != null) {
                for (TestItem item : generalList) {
                    if (item.isActive()) {
                        item.setActive(false);
                        selectedItems.add(item);
                    }
                }
            }

            if (advancedList != null) {
                for (TestItem item : advancedList) {
                    if (item.isActive()) {
                        item.setActive(false);
                        selectedItems.add(item);
                    }
                }
            }
        }

        return selectedItems;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
