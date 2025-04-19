package com.fmv.healthkiosk.ui.home.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
import com.fmv.healthkiosk.databinding.FragmentHomeLandingBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.ui.home.landing.adapters.MenuItemAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeLandingFragment extends BaseFragment<FragmentHomeLandingBinding, HomeLandingViewModel> {

    private MenuItemAdapter menuItemAdapter = new MenuItemAdapter();

    @Override
    protected Class<HomeLandingViewModel> getViewModelClass() {
        return HomeLandingViewModel.class;
    }

    @Override
    protected FragmentHomeLandingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHomeLandingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setListeners();
    }

    private void observeViewModel() {
        viewModel.username.observe(this, username -> binding.tvTitlePinType.setText(getString(R.string.fragment_home_welcome_name, username)));

        viewModel.pagedMenuItems.observe(getViewLifecycleOwner(), menuItemAdapter::submitList);

        viewModel.showNextMenuButton.observe(getViewLifecycleOwner(), show -> binding.btnNextMenu.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackMenuButton.observe(getViewLifecycleOwner(), show -> binding.btnBackMenu.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
    }

    private void setListeners() {
        menuItemAdapter.setOnItemClickListener((menuItem, position) -> handleMenuClick(menuItem.getId()));

        binding.btnNextMenu.setOnClickListener(v -> viewModel.nextPage());
        binding.btnBackMenu.setOnClickListener(v -> viewModel.previousPage());

        binding.rvMenu.setAdapter(menuItemAdapter);
        binding.rvMenu.addItemDecoration(new MarginItemDecoration(0, 42, 0, 0, MarginItemDecoration.LastPaddingToBeExcluded.RIGHT));
        binding.rvMenu.setLayoutManager(new GridLayoutManager(requireContext(), 3, GridLayoutManager.VERTICAL, false));
    }

    private void handleMenuClick(String menuId) {
        switch (menuId) {
            case "general_checkup":
//                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationMedicalPackage(), false);
                MedicalPackage medicalPackage = new MedicalPackage(getString(R.string.fragment_customize_test_general_checkup), "", "", 0);
                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationCustomizeTest(medicalPackage), false);
                break;
            case "advanced_test":
                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationMedicalPackage(), false);
                break;
            case "report":
                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationTestReportHistory(), false);
                break;
//            case "e_health_record":
//                break;
            case "telemedicine":
                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationTelemedicineFragment(), false);
                break;
        }
    }
}
