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

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        if (testViewModel.selectedTestItem.getValue().getTestResult() != null) {
            if (!testViewModel.selectedTestItem.getValue().getTestResult().isEmpty()) {
                String stethoscopeData = testViewModel.selectedTestItem.getValue().getTestResult();

                try {
                    String[] values = stethoscopeData.split(",");
                    float ecgData1 = Float.parseFloat(values[0].trim());
                    float ecgData2 = Float.parseFloat(values[1].trim());
                    float ecgData3 = Float.parseFloat(values[2].trim());
                    float ecgData4 = Float.parseFloat(values[3].trim());
                    int bpm = Integer.parseInt(values[4].trim());
                    String recordingTime = values[5].trim();

                    viewModel.ecgData1.setValue(ecgData1);
                    viewModel.ecgData2.setValue(ecgData2);
                    viewModel.ecgData3.setValue(ecgData3);
                    viewModel.ecgData4.setValue(ecgData4);
                    viewModel.bpm.setValue(bpm);
                    viewModel.recordingTime.setValue(recordingTime);
                } catch (Exception e) {
                    resetState();
                }
            } else {
                resetState();
            }
        } else {
            resetState();
        }

        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "stethoscope")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    Log.e("FTEST", "--> Updated to " + testItem.isTested());

                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            String stethoscopeData = testViewModel.selectedTestItem.getValue().getTestResult();

                            try {
                                String[] values = stethoscopeData.split(",");
                                float ecgData1 = Float.parseFloat(values[0].trim());
                                float ecgData2 = Float.parseFloat(values[1].trim());
                                float ecgData3 = Float.parseFloat(values[2].trim());
                                float ecgData4 = Float.parseFloat(values[3].trim());
                                int bpm = Integer.parseInt(values[4].trim());
                                String recordingTime = values[5].trim();

                                Log.e("FTEST", "--> Setting Up");

                                viewModel.ecgData1.setValue(ecgData1);
                                viewModel.ecgData2.setValue(ecgData2);
                                viewModel.ecgData3.setValue(ecgData3);
                                viewModel.ecgData4.setValue(ecgData4);
                                viewModel.bpm.setValue(bpm);
                                viewModel.recordingTime.setValue(recordingTime);
                            } catch (Exception e) {
                                resetState();
                            }
                        }

                        binding.layoutContent.setVisibility(View.VISIBLE);
                        binding.layoutHeader.setVisibility(View.GONE);
                    } else {
                        binding.layoutContent.setVisibility(isHeaderShowed ? View.VISIBLE : View.GONE);
                        binding.layoutHeader.setVisibility(isHeaderShowed ? View.GONE : View.VISIBLE);
                    }
                });
            }
        });

        viewModel.ecgData1.observe(getViewLifecycleOwner(), data -> {
                    Log.e("FTEST", "observeViewModel: " + data);
                    binding.ecgView1.addECGData(data);
                }
        );
        viewModel.ecgData2.observe(getViewLifecycleOwner(), data -> binding.ecgView2.addECGData(data));
        viewModel.ecgData3.observe(getViewLifecycleOwner(), data -> binding.ecgView3.addECGData(data));
        viewModel.ecgData4.observe(getViewLifecycleOwner(), data -> binding.ecgView4.addECGData(data));

        viewModel.bpm.observe(getViewLifecycleOwner(), bpm -> binding.tvBpm.setText(String.valueOf(bpm)));
        viewModel.recordingTime.observe(getViewLifecycleOwner(), time -> binding.tvRecordingTime.setText(time));
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.stethoscope_steps);

        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < stepsArray.length; i++) {
            formattedText.append(i + 1).append(". ").append(stepsArray[i]).append("\n");
        }

        binding.tvSteps.setText(formattedText.toString().trim());
    }

    private void setListeners() {
        binding.btnStartHeader.setOnClickListener(v -> {
            viewModel.isHeaderShowed.setValue(true);
        });

        binding.btnStart.setOnClickListener(v -> {
            testViewModel.updateTestItem("stethoscope", 1, null);
            viewModel.startECGSimulation();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("stethoscope", 2, viewModel.ecgData1.getValue() + "," + viewModel.ecgData2.getValue() + "," + viewModel.ecgData3.getValue() + "," + viewModel.ecgData4.getValue() + "," + viewModel.bpm.getValue() + "," + viewModel.recordingTime.getValue());
            viewModel.stopECGSimulation();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("stethoscope", 0, null);
            viewModel.resetECG();
        });
    }

    private void resetState() {
        viewModel.ecgData1.setValue(0F);
        viewModel.ecgData2.setValue(0F);
        viewModel.ecgData3.setValue(0F);
        viewModel.ecgData4.setValue(0F);
        viewModel.bpm.setValue(0);
        viewModel.recordingTime.setValue("00:00");
    }
}
