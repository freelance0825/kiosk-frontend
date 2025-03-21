package com.fmv.healthkiosk.feature.tests.domain.repo;

import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;

import java.util.List;

import io.reactivex.Single;

public interface TestsRepository {
    Single<List<MedicalPackage>> getMedicalPackages();

    List<TestItem> getTestItems(String testsPreset);
}