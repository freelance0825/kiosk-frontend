package com.fmv.healthkiosk.ui.telemedicine.notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentNotificationBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.ui.telemedicine.notification.adapters.AllNotificationAdapter;
import com.fmv.healthkiosk.ui.telemedicine.notification.adapters.TodayNotificationAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class NotificationFragment extends BaseFragment<FragmentNotificationBinding, NotificationViewModel> {

    private final TodayNotificationAdapter todayNotificationAdapter = new TodayNotificationAdapter();
    private final AllNotificationAdapter allNotificationAdapter = new AllNotificationAdapter();

    @Override
    protected Class<NotificationViewModel> getViewModelClass() {
        return NotificationViewModel.class;
    }

    @Override
    protected FragmentNotificationBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentNotificationBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.username.observe(getViewLifecycleOwner(), username -> {
            todayNotificationAdapter.setUsername(username);
            allNotificationAdapter.setUsername(username);
        });

        viewModel.todayNotificationList.observe(getViewLifecycleOwner(), todayNotificationAdapter::submitList);
        viewModel.allNotificationList.observe(getViewLifecycleOwner(), allNotificationAdapter::submitList);
    }

    private void setViews() {
        binding.rvTodayNotification.setAdapter(todayNotificationAdapter);
        binding.rvTodayNotification.setLayoutManager(new LinearLayoutManager(requireContext()));

        binding.rvAllNotification.setAdapter(allNotificationAdapter);
        binding.rvAllNotification.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        todayNotificationAdapter.setOnItemClickListener(new TodayNotificationAdapter.OnItemClickListener() {
            @Override
            public void onJoinNowClick(Notification doctor, int position) {
                navigateToFragment(NotificationFragmentDirections.actionNavigationNotificationFragmentToNavigationVideoCall(doctor.getDoctor()), false);
            }

            @Override
            public void onConfirmClick(Notification doctor, int position) {

            }
        });
    }
}
