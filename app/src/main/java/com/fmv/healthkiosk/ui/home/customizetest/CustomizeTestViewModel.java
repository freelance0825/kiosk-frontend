package com.fmv.healthkiosk.ui.home.customizetest;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.usecase.MedicalPackageUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class CustomizeTestViewModel extends BaseViewModel {

    MedicalPackage medicalPackage;

    private final TestsPresetUseCase testsPresetUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<TestItem>> testItemList = new MutableLiveData<>();


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
        testItemList.setValue(testsPresetUseCase.getTestItems(testPresets));
    }

    public void toggleTestItem(TestItem testItem) {
        List<TestItem> currentList = testItemList.getValue();

        if (currentList == null) return;

        List<TestItem> updatedList = new ArrayList<>();
        for (TestItem item : currentList) {
            if (item.getId().equals(testItem.getId())) {
                item.setActive(!item.isActive());
            }
            updatedList.add(item);
        }

        testItemList.setValue(updatedList);
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


    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
