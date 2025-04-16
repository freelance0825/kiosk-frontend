package com.fmv.healthkiosk.ui.home.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentHomeLandingBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.ui.home.landing.adapters.MenuItemAdapter;
import com.fmv.healthkiosk.ui.home.model.MenuItem;

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
        if (!viewModel.menuList.isEmpty()) {
            menuItemAdapter.submitList(viewModel.menuList);
        }

        viewModel.username.observe(this, username -> binding.tvTitlePinType.setText(getString(R.string.fragment_home_welcome_name, username)));
    }

    private void setListeners() {
        menuItemAdapter.setOnItemClickListener((menuItem, position) -> handleMenuClick(menuItem.getId()));

        binding.rvMenu.setAdapter(menuItemAdapter);
        binding.rvMenu.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
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
                break;
//            case "e_health_record":
//                break;
            case "telemedicine":
                navigateToFragment(HomeLandingFragmentDirections.actionNavigationHomeLandingToNavigationTelemedicineFragment(), false);
                break;
        }
    }
}
