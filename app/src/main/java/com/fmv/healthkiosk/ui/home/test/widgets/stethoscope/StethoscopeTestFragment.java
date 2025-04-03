package com.fmv.healthkiosk.ui.home.test.widgets.stethoscope;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentOximeterTestBinding;
import com.fmv.healthkiosk.databinding.FragmentStethoscopeTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;
import com.fmv.healthkiosk.ui.home.test.widgets.oximeter.OximeterTestViewModel;

import java.util.Objects;
import java.util.Random;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class StethoscopeTestFragment extends BaseFragment<FragmentStethoscopeTestBinding, StethoscopeTestViewModel> {

    private TestViewModel testViewModel;



    @Override
    protected Class<StethoscopeTestViewModel> getViewModelClass() {
        return StethoscopeTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentStethoscopeTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentStethoscopeTestBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        testViewModel = new ViewModelProvider(requireActivity()).get(TestViewModel.class);

        observeViewModel();
//
//        setViews();
        setListeners();

    }

    private void observeViewModel() {
        viewModel.getEcgData1().observe(getViewLifecycleOwner(), data -> binding.ecgView1.addECGData(data));
        viewModel.getEcgData2().observe(getViewLifecycleOwner(), data -> binding.ecgView2.addECGData(data));
        viewModel.getEcgData3().observe(getViewLifecycleOwner(), data -> binding.ecgView3.addECGData(data));
        viewModel.getEcgData4().observe(getViewLifecycleOwner(), data -> binding.ecgView4.addECGData(data));

        viewModel.getBpm().observe(getViewLifecycleOwner(), bpm -> binding.tvBpm.setText(String.valueOf(bpm)));
        viewModel.getRecordingTime().observe(getViewLifecycleOwner(), time -> binding.tvRecordingTime.setText(time));
    }

//    private void setViews() {
//        String[] stepsArray = getResources().getStringArray(R.array.oximeter_steps);
//
//        StringBuilder formattedText = new StringBuilder();
//        for (int i = 0; i < stepsArray.length; i++) {
//            formattedText.append(i + 1).append(". ").append(stepsArray[i]).append("\n");
//        }
//
//        binding.tvSteps.setText(formattedText.toString().trim());
//    }
//
    private void setListeners() {
        binding.btnStartHeader.setOnClickListener(v -> {
//            viewModel.isHeaderShowed.setValue(true);
        });

        binding.btnStart.setOnClickListener(v -> {
            Log.e("FTEST", "setListeners: CLICKED" );
            viewModel.startECGSimulation();
//            testViewModel.updateTestItem("oximeter", 1, null);
//            viewModel.startSimulatedOximeter();
        });

        binding.btnStop.setOnClickListener(v -> {
            viewModel.stopECGSimulation();
//            testViewModel.updateTestItem("oximeter", 2, viewModel.oxygenData.getValue() + "," + viewModel.pulseData.getValue());
//            viewModel.stopSimulatedOximeter();
        });

        binding.btnRetake.setOnClickListener(v -> {
            viewModel.stopECGSimulation();
//            testViewModel.updateTestItem("oximeter", 0, null);
//            viewModel.resetOximeter();
        });
    }
}
