package com.fmv.healthkiosk.ui.home.customizetest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentCustomizeTestBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.CustomPackageTestAdapter;
import com.fmv.healthkiosk.ui.home.customizetest.adapters.PackageTestAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class CustomizeTestFragment extends BaseFragment<FragmentCustomizeTestBinding, CustomizeTestViewModel> {

    private final PackageTestAdapter packageTestAdapterGeneral = new PackageTestAdapter();
    private final PackageTestAdapter packageTestAdapterAdvanced = new PackageTestAdapter();
    private final CustomPackageTestAdapter customPackageTestAdapterGeneral = new CustomPackageTestAdapter();
    private final CustomPackageTestAdapter customPackageTestAdapterAdvanced = new CustomPackageTestAdapter();

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
            binding.tvTitle.setText(getString(R.string.fragment_customize_test_test_on, viewModel.medicalPackage.getName()));
        } else {
            binding.tvTitle.setText(getString(com.fmv.healthkiosk.R.string.fragment_customize_test_custom_test));
        }

        viewModel.testItemListGeneral.observe(getViewLifecycleOwner(), testItems -> {
            packageTestAdapterGeneral.submitList(testItems);
            customPackageTestAdapterGeneral.submitList(testItems);
        });

        viewModel.testItemListAdvanced.observe(getViewLifecycleOwner(), testItems -> {
            if (!testItems.isEmpty()) {
                binding.tvAdvanced.setVisibility(View.VISIBLE);
            }

            packageTestAdapterAdvanced.submitList(testItems);
            customPackageTestAdapterAdvanced.submitList(testItems);
        });
    }

    private void setViews() {
        if (viewModel.medicalPackage != null) {
            binding.rvGeneralCheckup.setAdapter(packageTestAdapterGeneral);
            binding.rvGeneralCheckup.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
            binding.rvAdvancedTest.setAdapter(packageTestAdapterAdvanced);
            binding.rvAdvancedTest.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
        } else {
            binding.rvGeneralCheckup.setAdapter(customPackageTestAdapterGeneral);
            binding.rvGeneralCheckup.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
            binding.rvAdvancedTest.setAdapter(customPackageTestAdapterAdvanced);
            binding.rvAdvancedTest.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
        }
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        customPackageTestAdapterGeneral.setOnItemClickListener((testItem, position) -> {
            viewModel.toggleTestItemGeneral(testItem);
            customPackageTestAdapterGeneral.notifyItemChanged(position);
        });

        customPackageTestAdapterAdvanced.setOnItemClickListener((testItem, position) -> {
            viewModel.toggleTestItemAdvanced(testItem);
            customPackageTestAdapterAdvanced.notifyItemChanged(position);
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
