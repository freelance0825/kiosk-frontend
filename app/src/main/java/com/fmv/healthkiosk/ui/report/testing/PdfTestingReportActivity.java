package com.fmv.healthkiosk.ui.report.testing;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseActivity;
import com.fmv.healthkiosk.databinding.ActivityPdfTestingReportBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.ui.report.testing.adapter.PdfTestResultAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PdfTestingReportActivity extends BaseActivity<ActivityPdfTestingReportBinding, PdfTestingReportViewModel> {

    private final PdfTestResultAdapter generalTestAdapter = new PdfTestResultAdapter();
    private final PdfTestResultAdapter advancedTestAdapter = new PdfTestResultAdapter();

    public static final String EXTRA_TESTING_RESULT = "extra_testing_result";

    @Override
    protected Class<PdfTestingReportViewModel> getViewModelClass() {
        return PdfTestingReportViewModel.class;
    }

    @Override
    protected ActivityPdfTestingReportBinding getViewBinding() {
        return ActivityPdfTestingReportBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        extractIntentExtras();

        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.testHistoryModel.observe(this, testHistoryModel -> {
            if (testHistoryModel != null) {
                if (testHistoryModel.getPackageName().toLowerCase().contains("custom")) {
                    binding.tvPackageTitle.setText(getString(R.string.activity_pdf_testing_report_custom_test_report));
                    binding.layoutPackageName.setVisibility(View.GONE);
                } else {
                    binding.tvPackageTitle.setText(getString(R.string.activity_pdf_testing_report_medical_test_report));
                    binding.tvPackageName.setText(testHistoryModel.getPackageName());
                    binding.layoutPackageName.setVisibility(View.VISIBLE);
                }

                binding.tvPatientName.setText(testHistoryModel.getUserName());
                binding.tvPatientAddress.setText(testHistoryModel.getUserAddress());
                binding.tvReportDate.setText(testHistoryModel.getCreatedAt());

                String genderAge = String.format("%s, %s years old", testHistoryModel.getUserGender(), testHistoryModel.getUserAge());

                binding.tvPatientGenderAge.setText(genderAge);
                binding.tvPatientMobileNumber.setText(getString(R.string.activity_pdf_prescription_report_mob_no, testHistoryModel.getUserPhoneNumber()));
                binding.tvPatientId.setText(getString(R.string.activity_pdf_prescription_report_patient_id, String.valueOf(testHistoryModel.getUserId())));

            }
        });

        viewModel.generalTestList.observe(this, generalTests -> {
            if (!generalTests.isEmpty()) {
                generalTestAdapter.submitList(generalTests);
                binding.generalCheckupLayout.setVisibility(View.VISIBLE);
            }
        });

        viewModel.advancedTestList.observe(this, advancedTests -> {
            if (!advancedTests.isEmpty()) {
                advancedTestAdapter.submitList(advancedTests);
                binding.advancedTestLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setViews() {
        float healthScore = 90f; // Replace with actual logic from testingResults or ViewModel

        binding.donutChart.setProgress(healthScore);
        binding.tvHealthScorePercentage.setText(String.valueOf(healthScore));

        binding.rvGeneralCheckup.setAdapter(generalTestAdapter);
        binding.rvGeneralCheckup.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        binding.rvAdvancedTest.setAdapter(advancedTestAdapter);
        binding.rvAdvancedTest.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void extractIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            TestHistoryModel testHistoryModel = bundle.getParcelable(EXTRA_TESTING_RESULT);
            viewModel.setData(testHistoryModel);
        }
    }
}
