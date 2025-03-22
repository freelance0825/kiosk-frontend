package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;

import java.util.List;

public interface TestResultUseCase {
    List<TestResult> mapToTestResults(List<TestItem> testItems);
}
