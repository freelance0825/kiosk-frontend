package com.fmv.healthkiosk.ui.telemedicine.chat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.FragmentChatBinding;
import com.fmv.healthkiosk.databinding.FragmentVideoCallBinding;
import com.fmv.healthkiosk.ui.telemedicine.videocall.VideoCallViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ChatFragment extends BaseFragment<FragmentChatBinding, ChatViewModel> {

    @Override
    protected Class<ChatViewModel> getViewModelClass() {
        return ChatViewModel.class;
    }

    @Override
    protected FragmentChatBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentChatBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {

    }

    private void setViews() {
        if (viewModel.doctorModel.getImageBase64() == null) {
            binding.ivDoctor.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.asset_dummy_doctor));
        } else {
            binding.ivDoctor.setImageBitmap(Base64Helper.convertToBitmap(viewModel.doctorModel.getImageBase64()));
        }

        binding.tvDoctorName.setText(viewModel.doctorModel.getName());
    }

    private void setListeners() {
        binding.btnFinish.setOnClickListener(v -> {
            navigateBack();
        });
    }
}
