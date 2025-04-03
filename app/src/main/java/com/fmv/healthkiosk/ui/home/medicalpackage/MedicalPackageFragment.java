package com.fmv.healthkiosk.ui.home.medicalpackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentMedicalPackageBinding;
import com.fmv.healthkiosk.ui.home.medicalpackage.adapters.MedicalPackageAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MedicalPackageFragment extends BaseFragment<FragmentMedicalPackageBinding, MedicalPackageViewModel> {

    private MedicalPackageAdapter medicalPackageAdapter = new MedicalPackageAdapter();

    @Override
    protected Class<MedicalPackageViewModel> getViewModelClass() {
        return MedicalPackageViewModel.class;
    }

    @Override
    protected FragmentMedicalPackageBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentMedicalPackageBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {

        });

        viewModel.medicalPackageList.observe(this, medicalPackageList -> {
            medicalPackageAdapter.submitList(medicalPackageList);
        });
    }

    private void setViews() {
        int spanCount = 3;
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), spanCount);
//        layoutManager.setSmoothScrollbarEnabled(false);

        binding.rvMedicalPackages.setLayoutManager(layoutManager);
        binding.rvMedicalPackages.setAdapter(medicalPackageAdapter);
//        binding.rvMedicalPackages.setNestedScrollingEnabled(false);
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.btnStartTest.setOnClickListener(v -> navigateToFragment(MedicalPackageFragmentDirections.actionNavigationMedicalPackageToNavigationCustomizeTest(null), false));

        medicalPackageAdapter.setOnItemClickListener(medicalPackage -> {
            navigateToFragment(MedicalPackageFragmentDirections.actionNavigationMedicalPackageToNavigationCustomizeTest(medicalPackage), false);
        });
    }
}
