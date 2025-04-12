package com.fmv.healthkiosk.ui.home.testreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentTestReportBinding;
import com.fmv.healthkiosk.ui.home.testreport.adapter.TestResultAdapter;
import com.fmv.healthkiosk.ui.report.testing.PdfTestingReportActivity;

import java.util.ArrayList;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestReportFragment extends BaseFragment<FragmentTestReportBinding, TestReportViewModel> {

    private TestResultAdapter testResultAdapter = new TestResultAdapter();

    @Override
    protected Class<TestReportViewModel> getViewModelClass() {
        return TestReportViewModel.class;
    }

    @Override
    protected FragmentTestReportBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestReportBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        if (viewModel.medicalPackage != null) {
            binding.tvTitle.setText(getString(R.string.fragment_test_report_report, viewModel.medicalPackage.getName()));
        } else {
            binding.tvTitle.setText(getString(R.string.fragment_test_report_report, getString(R.string.fragment_test_custom_test)));
        }

        viewModel.inflatedTestResult.observe(getViewLifecycleOwner(), testResults -> {
            testResultAdapter.submitList(testResults);
        });
    }

    private void setViews() {
        binding.rvTestResult.setAdapter(testResultAdapter);
        binding.rvTestResult.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.rangeBar.setLabelTextSize(20F);
        binding.rangeBar.setTickValues(100, 120);
        binding.rangeBar.setCenterLabel("Normal");
        binding.rangeBar.setProgressRange(0.2f, 0.7f); // Customize range dynamically


        binding.btnExportToPdf.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(PdfTestingReportActivity.EXTRA_TESTING_RESULT, new ArrayList<>(Objects.requireNonNull(viewModel.inflatedTestResult.getValue())));
            bundle.putParcelable(PdfTestingReportActivity.EXTRA_PACKAGE_NAME, viewModel.medicalPackage);

            navigateToActivity(PdfTestingReportActivity.class, bundle);
        });
    }
}
