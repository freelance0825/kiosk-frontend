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
    public static final String EXTRA_PACKAGE_NAME = "extra_package_name";

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
        viewModel.medicalPackage.observe(this, medicalPackage -> {
            String formattedDate = new SimpleDateFormat("dd MMMM yyyy, 'at' hh:mm a", Locale.getDefault()).format(new Date());
            binding.tvReportDate.setText(formattedDate);

            if (medicalPackage != null) {
                binding.tvPackageName.setText(getString(R.string.fragment_pdf_testing_report_package_name, medicalPackage.getName()));
            } else {
                binding.tvPackageName.setText(getString(R.string.fragment_pdf_testing_report_package_name, getString(R.string.fragment_test_custom_test)));
            }
        });

        viewModel.username.observe(this, binding.tvPatientName::setText);

        viewModel.gender.observe(this, gender -> {
            viewModel.age.observe(this, age -> {
                String genderAge = String.format("%s, %d years old", gender, age);

                binding.tvPatientGenderAge.setText(genderAge);
            });
        });

        viewModel.phoneNumber.observe(this, phoneNumber -> {
            binding.tvPatientMobileNumber.setText(getString(R.string.activity_pdf_prescription_report_mob_no, phoneNumber));
        });


        viewModel.userId.observe(this, userId -> {
            binding.tvPatientId.setText(getString(R.string.activity_pdf_prescription_report_patient_id, String.valueOf(userId)));
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
            ArrayList<TestResult> testingResults = bundle.getParcelableArrayList(EXTRA_TESTING_RESULT);
            MedicalPackage medicalPackage = bundle.getParcelable(EXTRA_PACKAGE_NAME);

            if (testingResults == null) testingResults = new ArrayList<>();
            viewModel.setData(testingResults, medicalPackage);
        }
    }

}
