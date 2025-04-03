package com.fmv.healthkiosk.ui.home.testreport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentCustomizeTestBinding;
import com.fmv.healthkiosk.databinding.FragmentTestReportBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.ui.home.customizetest.CustomizeTestFragmentDirections;
import com.fmv.healthkiosk.ui.home.customizetest.CustomizeTestViewModel;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.CustomPackageTestAdapter;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.PackageTestAdapter;
import com.fmv.healthkiosk.ui.home.testreport.adapter.TestResultAdapter;

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

        binding.btnExportToPdf.setOnClickListener(v -> {

        });
    }
}
