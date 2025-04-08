package com.fmv.healthkiosk.ui.telemedicine.landing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentHomeLandingBinding;
import com.fmv.healthkiosk.databinding.FragmentTelemedicineLandingBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.ui.home.landing.HomeLandingFragmentDirections;
import com.fmv.healthkiosk.ui.home.landing.HomeLandingViewModel;
import com.fmv.healthkiosk.ui.home.landing.adapters.MenuItemAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TelemedicineLandingFragment extends BaseFragment<FragmentTelemedicineLandingBinding, TelemedicineLandingViewModel> {

    private MenuItemAdapter menuItemAdapter = new MenuItemAdapter();

    @Override
    protected Class<TelemedicineLandingViewModel> getViewModelClass() {
        return TelemedicineLandingViewModel.class;
    }

    @Override
    protected FragmentTelemedicineLandingBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTelemedicineLandingBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setListeners();
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNotification.setOnClickListener(v -> {
            navigateToFragment(TelemedicineLandingFragmentDirections.actionNavigationTelemedicineFragmentToNavigationNotificationFragment(), false);
        });

        binding.btnConsultNow.setOnClickListener(v -> {
            navigateToFragment(TelemedicineLandingFragmentDirections.actionNavigationTelemedicineFragmentToNavigationConsultNow(), false);
        });

        binding.btnMyAppointment.setOnClickListener(v -> {
            navigateToFragment(TelemedicineLandingFragmentDirections.actionNavigationTelemedicineFragmentToNavigationMyAppointmentFragment(), false);
        });

        binding.btnConsultationHistory.setOnClickListener(v -> {
//            navigate(TelemedicineLandingFragmentDirections.actionTelemedicineLandingFragmentToTelemedicineConsultNowFragment());
        });
    }
}
