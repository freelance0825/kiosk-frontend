package com.fmv.healthkiosk.feature.tests.data.repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.util.Log;

import com.fmv.healthkiosk.feature.tests.R;
import com.fmv.healthkiosk.feature.tests.data.mapper.TestResultMapper;
import com.fmv.healthkiosk.feature.tests.data.source.local.TestItems;
import com.fmv.healthkiosk.feature.tests.data.source.remote.TestsService;
import com.fmv.healthkiosk.feature.tests.data.source.remote.model.MedicalPackageResponseItem;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Single;

public class TestsRepositoryImpl implements TestsRepository {

    private final TestsService testsService;

    @Inject
    public TestsRepositoryImpl(Context context, TestsService testsService) {
        this.testsService = testsService;
    }

    @Override
    public Single<List<MedicalPackage>> getMedicalPackages() {
        return testsService.getMedicalPackages()
                .map(this::mapToMedicalPackages);
    }

    @Override
    public List<TestItem> getTestItems(String testsPreset) {
        if (testsPreset == null || testsPreset.isEmpty()) {
            return TestItems.testItemList;
        } else {
            List<TestItem> filteredList = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(testsPreset);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String testName = jsonArray.getString(i);
                    for (TestItem item : TestItems.testItemList) {
                        if (item.getName().equalsIgnoreCase(testName)) {
                            filteredList.add(item);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                Log.e("TestRepository", "getTestItems: " + e.getMessage());
            }
            return filteredList;
        }
    }

    private List<MedicalPackage> mapToMedicalPackages(List<MedicalPackageResponseItem> items) {
        return items == null ? Collections.emptyList() :
                items.stream()
                        .map(item -> new MedicalPackage(item.getName(), item.getIcon(), item.getTests(), item.getId()))
                        .collect(Collectors.toList());
    }

    @Override
    public List<TestResult> mapToTestResults(List<TestItem> testItems) {
        return new TestResultMapper().mapToTestResults(testItems);
    }
}
