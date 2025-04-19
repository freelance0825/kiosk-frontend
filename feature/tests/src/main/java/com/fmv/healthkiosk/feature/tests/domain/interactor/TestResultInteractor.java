package com.fmv.healthkiosk.feature.tests.domain.interactor;

import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.feature.tests.domain.model.TestsResultModel;
import com.fmv.healthkiosk.feature.tests.domain.repo.TestsRepository;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestResultUseCase;
import com.fmv.healthkiosk.feature.tests.domain.usecase.TestsPresetUseCase;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class TestResultInteractor implements TestResultUseCase {
    private final TestsRepository testsRepository;

    @Inject
    public TestResultInteractor(TestsRepository testsRepository) {
        this.testsRepository = testsRepository;
    }

    @Override
    public List<TestsResultModel> mapToTestResults(List<TestItem> testItems) {
        return testsRepository.mapToTestResults(testItems);
    }
}
