package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;

import java.util.List;

public interface TestResultUseCase {
    List<TestsResultModel> mapToTestResults(List<TestItem> testItems);
}
