package com.fmv.healthkiosk.ui.telemedicine.videocall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.facebook.react.modules.core.PermissionListener;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentVideoCallBinding;

import org.jitsi.meet.sdk.JitsiMeetActivityInterface;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetUserInfo;
import org.jitsi.meet.sdk.JitsiMeetView;


import java.net.MalformedURLException;
import java.net.URL;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class VideoCallFragment extends BaseFragment<FragmentVideoCallBinding, VideoCallViewModel> {

    private JitsiMeetView jitsiMeetView;

    @Override
    protected Class<VideoCallViewModel> getViewModelClass() {
        return VideoCallViewModel.class;
    }

    @Override
    protected FragmentVideoCallBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentVideoCallBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
//        viewModel.timeString.observe(getViewLifecycleOwner(), timeString -> {
//            binding.tvTimeCounter.setText(timeString);
//        });
//
//        viewModel.username.observe(getViewLifecycleOwner(), username -> {
//            binding.tvPatientName.setText(username);
//        });
    }

    private void setViews() {
        try {
            jitsiMeetView = new JitsiMeetView(requireActivity());

            JitsiMeetUserInfo userInfo = new JitsiMeetUserInfo();
            userInfo.setDisplayName(viewModel.username.getValue());

            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(new URL("https://meet.jit.si"))
                    .setRoom("healthKiosk12012") // Replace with your actual room name
                    .setAudioMuted(false)
                    .setUserInfo(userInfo)
                    .setVideoMuted(false)
                    .build();

            jitsiMeetView.join(options);
            binding.jitsiMeetView.addView(jitsiMeetView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Invalid Jitsi Meet server URL", Toast.LENGTH_SHORT).show();
        }

//        binding.tvDoctorName.setText(viewModel.doctorForNotification.getName());
//        binding.tvDoctorSpecialization.setText(viewModel.doctorForNotification.getSpecialization());
    }

    private void setListeners() {
//        binding.btnEndCall.setOnClickListener(v -> {
//            navigateBack();
//        });
    }

    @Override
    public void onDestroyView() {
        if (jitsiMeetView != null) {
            jitsiMeetView.dispose();
            jitsiMeetView = null;
        }
        super.onDestroyView();
    }
}
