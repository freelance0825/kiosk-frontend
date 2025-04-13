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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
            Log.e("FTEST", "getTestItemsCCC: " + testsPreset);

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
                        .map(item -> new MedicalPackage(item.getName(), item.getIcon(), mapTestPresets(item.getTests()), item.getId()))
                        .collect(Collectors.toList());
    }

    private String mapTestPresets(String testPreset) {
        Log.e("FTEST", "getTestItemsAAA: " + testPreset);

        // Regex pattern to match "name": "<value>" in the format {"name":"<value>"}
        Pattern pattern = Pattern.compile("name=([^,}\\]]+)");
        Matcher matcher = pattern.matcher(testPreset);

        List<String> names = new ArrayList<>();

        // Extract names and convert them to lowercase
        while (matcher.find()) {
            String name = matcher.group(1).toLowerCase();
            names.add(name);
        }

        // Join the names into a single string in the desired format
        String result = "[" + String.join(", ", names) + "]";

        Log.e("FTEST", "getTestItemsBBB: " + result);

        return result;
    }


    @Override
    public List<TestResult> mapToTestResults(List<TestItem> testItems) {
        return new TestResultMapper().mapToTestResults(testItems);
    }
}
