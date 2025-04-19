package com.fmv.healthkiosk.ui.home.testreporthistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
import com.fmv.healthkiosk.databinding.FragmentTestReportHistoryBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.tests.domain.model.TestHistoryModel;
import com.fmv.healthkiosk.ui.home.testreporthistory.adapters.TestReportHistoryAdapter;
import com.fmv.healthkiosk.ui.telemedicine.consultationhistory.ConsultationHistoryFragmentDirections;
import com.fmv.healthkiosk.ui.telemedicine.consultationhistory.adapters.ConsultationHistoryAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestReportHistoryFragment extends BaseFragment<FragmentTestReportHistoryBinding, TestReportHistoryViewModel> {

    private final TestReportHistoryAdapter testReportHistoryAdapter = new TestReportHistoryAdapter();

    @Override
    protected Class<TestReportHistoryViewModel> getViewModelClass() {
        return TestReportHistoryViewModel.class;
    }

    @Override
    protected FragmentTestReportHistoryBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestReportHistoryBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.pagedReportItems.observe(getViewLifecycleOwner(), testReportHistoryAdapter::submitList);

        viewModel.showNextReportButton.observe(getViewLifecycleOwner(), show -> binding.btnNextReport.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackReportButton.observe(getViewLifecycleOwner(), show -> binding.btnBackReport.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
    }

    private void setViews() {
        binding.rvReport.setAdapter(testReportHistoryAdapter);
        binding.rvReport.addItemDecoration(new MarginItemDecoration(0, 0, 0, 36, MarginItemDecoration.LastPaddingToBeExcluded.BOTTOM));
        binding.rvReport.setLayoutManager(new GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNextReport.setOnClickListener(v -> viewModel.nextReportPage());
        binding.btnBackReport.setOnClickListener(v -> viewModel.previousReportPage());

        testReportHistoryAdapter.setOnItemClickListener((testHistoryModel, position) -> navigateToFragment(TestReportHistoryFragmentDirections.actionNavigationTestReportHistoryToNavigationTestReport(testHistoryModel), false));
    }
}
