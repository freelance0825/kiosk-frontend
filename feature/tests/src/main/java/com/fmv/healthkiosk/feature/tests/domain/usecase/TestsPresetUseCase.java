package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;

import java.util.List;

import io.reactivex.Single;

public interface TestsPresetUseCase {
    List<TestItem> getTestItems(String testsPreset);
}
