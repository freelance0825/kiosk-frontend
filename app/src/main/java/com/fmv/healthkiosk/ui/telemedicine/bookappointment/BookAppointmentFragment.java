package com.fmv.healthkiosk.ui.telemedicine.bookappointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.customview.MarginItemDecoration;
import com.fmv.healthkiosk.databinding.FragmentBookAppointmentBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.ui.telemedicine.bookappointment.adapters.BookAppointmentAdapter;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BookAppointmentFragment extends BaseFragment<FragmentBookAppointmentBinding, BookAppointmentViewModel> {

    private final BookAppointmentAdapter bookAppointmentAdapter = new BookAppointmentAdapter();

    @Override
    protected Class<BookAppointmentViewModel> getViewModelClass() {
        return BookAppointmentViewModel.class;
    }

    @Override
    protected FragmentBookAppointmentBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBookAppointmentBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.pagedDoctorItems.observe(getViewLifecycleOwner(), bookAppointmentAdapter::submitList);

        viewModel.showNextDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnNextDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));
        viewModel.showBackDoctorButton.observe(getViewLifecycleOwner(), show -> binding.btnBackDoctors.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE));

    }

    private void setViews() {
        binding.rvDoctors.setAdapter(bookAppointmentAdapter);
        binding.rvDoctors.addItemDecoration(new MarginItemDecoration(0, 0, 0, 36, MarginItemDecoration.LastPaddingToBeExcluded.BOTTOM));
        binding.rvDoctors.setLayoutManager(new GridLayoutManager(requireContext(), 4, GridLayoutManager.HORIZONTAL, false));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> {
            navigateBack();
        });

        binding.btnNextDoctors.setOnClickListener(v -> viewModel.nextDoctorPage());
        binding.btnBackDoctors.setOnClickListener(v -> viewModel.previousDoctorPage());

        binding.btnNotification.setOnClickListener(v -> {
            navigateToFragment(BookAppointmentFragmentDirections.actionNavigationBookAppointmentFragmentToNavigationNotificationFragment(), false);
        });

        bookAppointmentAdapter.setOnItemClickListener(new BookAppointmentAdapter.OnItemClickListener() {
            @Override
            public void onBookAppointmentClick(DoctorModel doctor, int position) {
                navigateToFragment(BookAppointmentFragmentDirections.actionNavigationBookAppointmentFragmentToNavigationMakeAppointmentFragment(doctor), false);
            }
        });
    }
}
