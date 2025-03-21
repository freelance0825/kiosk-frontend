package com.fmv.healthkiosk.ui.home.customizetest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentCustomizeTestBinding;
import com.fmv.healthkiosk.databinding.FragmentMedicalPackageBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.CustomPackageTestAdapter;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.PackageTestAdapter;
import com.fmv.healthkiosk.ui.home.medicalpackage.MedicalPackageViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomizeTestFragment extends BaseFragment<FragmentCustomizeTestBinding, CustomizeTestViewModel> {

    private PackageTestAdapter packageTestAdapter = new PackageTestAdapter();
    private CustomPackageTestAdapter customPackageTestAdapter = new CustomPackageTestAdapter();

    @Override
    protected Class<CustomizeTestViewModel> getViewModelClass() {
        return CustomizeTestViewModel.class;
    }

    @Override
    protected FragmentCustomizeTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentCustomizeTestBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        if (viewModel.medicalPackage != null) {
            binding.tvTitle.setText(getString(com.fmv.healthkiosk.R.string.fragment_customize_test_test_on, viewModel.medicalPackage.getName()));
        } else {
            binding.tvTitle.setText(getString(com.fmv.healthkiosk.R.string.fragment_customize_test_custom_test));
        }

        viewModel.testItemList.observe(getViewLifecycleOwner(), testItems -> {
            packageTestAdapter.submitList(testItems);
            customPackageTestAdapter.submitList(testItems);
        });
    }

    private void setViews() {
        if (viewModel.medicalPackage != null) {
            binding.rvGeneralCheckup.setAdapter(packageTestAdapter);
            binding.rvGeneralCheckup.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
        } else {
            binding.rvGeneralCheckup.setAdapter(customPackageTestAdapter);
            binding.rvGeneralCheckup.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
        }
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        customPackageTestAdapter.setOnItemClickListener((testItem, position) -> {
            viewModel.toggleTestItem(testItem);
            customPackageTestAdapter.notifyItemChanged(position);
        });

        binding.btnStartTest.setOnClickListener(v -> {
            TestItemList testItemList = new TestItemList(viewModel.getSelectedTestItems());
            MedicalPackage medicalPackage = viewModel.medicalPackage;

            if (testItemList.getTestItemList().isEmpty()) {
                Toast.makeText(requireContext(), "Select at least one Test", Toast.LENGTH_SHORT).show();
            } else {
                navigateToFragment(CustomizeTestFragmentDirections.actionNavigationCustomizeTestToNavigationTest(testItemList, medicalPackage), false);
            }
        });
    }
}
