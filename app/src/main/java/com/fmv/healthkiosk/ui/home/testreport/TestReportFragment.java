package com.fmv.healthkiosk.ui.home.testreport;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
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
        Log.e("FTEST", "observeViewModel TEstReport: " + viewModel.testHistoryModel.getPackageName() );

        if (viewModel.testHistoryModel.getPackageName().toLowerCase().contains("custom")) {
            binding.tvTitle.setText(getString(R.string.fragment_test_report_report, getString(R.string.fragment_test_custom_test)));
        } else {
            binding.tvTitle.setText(getString(R.string.fragment_test_report_report, viewModel.testHistoryModel.getPackageName()));
        }

        viewModel.pagedTestResult.observe(getViewLifecycleOwner(), testResults -> {
            testResultAdapter.submitList(testResults);
        });

        viewModel.showNextTestResultButton.observe(getViewLifecycleOwner(), show -> binding.btnNextTestResult.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackTestResultButton.observe(getViewLifecycleOwner(), show -> binding.btnBackTestResult.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
    }

    private void setViews() {
        binding.rvTestResult.setAdapter(testResultAdapter);
        binding.rvTestResult.addItemDecoration(new MarginItemDecoration(0, 36, 0, 0, MarginItemDecoration.LastPaddingToBeExcluded.RIGHT));
        binding.rvTestResult.setLayoutManager(new GridLayoutManager(requireContext(), 4, GridLayoutManager.VERTICAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.rangeBar.setLabelTextSize(20F);
        binding.rangeBar.setTickValues(100, 120);
        binding.rangeBar.setCenterLabel("Normal");
        binding.rangeBar.setProgressRange(0.2f, 0.7f); // Customize range dynamically


        binding.btnNextTestResult.setOnClickListener(v -> viewModel.nextTestResultPage());
        binding.btnBackTestResult.setOnClickListener(v -> viewModel.previousTestResultPage());

        binding.btnExportToPdf.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PdfTestingReportActivity.EXTRA_TESTING_RESULT, viewModel.testHistoryModel);

            navigateToActivity(PdfTestingReportActivity.class, bundle);
        });
    }
}
