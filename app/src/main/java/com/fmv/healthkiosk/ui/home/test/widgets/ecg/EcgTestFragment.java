package com.fmv.healthkiosk.ui.home.test.widgets.ecg;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentEcgTestBinding;
import com.fmv.healthkiosk.databinding.FragmentStethoscopeTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;
import com.fmv.healthkiosk.ui.home.test.widgets.stethoscope.StethoscopeTestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EcgTestFragment extends BaseFragment<FragmentEcgTestBinding, EcgTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<EcgTestViewModel> getViewModelClass() {
        return EcgTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentEcgTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentEcgTestBinding.inflate(inflater, container, false);
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
                String ecgData = testViewModel.selectedTestItem.getValue().getTestResult();

                try {
                    String[] values = ecgData.split(",");
                    float ecgData1 = Float.parseFloat(values[0].trim());
                    float ecgData2 = Float.parseFloat(values[1].trim());
                    float ecgData3 = Float.parseFloat(values[2].trim());
                    int bpm = Integer.parseInt(values[3].trim());
                    String recordingTime = values[4].trim();

                    viewModel.ecgDataI.setValue(ecgData1);
                    viewModel.ecgDataII.setValue(ecgData2);
                    viewModel.ecgDataIII.setValue(ecgData3);
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

            if (Objects.equals(testItem.getId(), "ecg")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    Log.e("FTEST", "--> Updated to " + testItem.isTested());

                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            String ecgData = testViewModel.selectedTestItem.getValue().getTestResult();

                            try {
                                String[] values = ecgData.split(",");
                                float ecgData1 = Float.parseFloat(values[0].trim());
                                float ecgData2 = Float.parseFloat(values[1].trim());
                                float ecgData3 = Float.parseFloat(values[2].trim());
                                int bpm = Integer.parseInt(values[3].trim());
                                String recordingTime = values[4].trim();

                                Log.e("FTEST", "--> Setting Up");

                                viewModel.ecgDataI.setValue(ecgData1);
                                viewModel.ecgDataII.setValue(ecgData2);
                                viewModel.ecgDataIII.setValue(ecgData3);
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

        viewModel.ecgDataI.observe(getViewLifecycleOwner(), data -> {
            binding.ecgViewI.addECGData(data);
            binding.ecgViewIAVR.addECGData(data);
        });

        viewModel.ecgDataII.observe(getViewLifecycleOwner(), data -> {
            binding.ecgViewII.addECGData(data);
            binding.ecgViewIIAVR.addECGData(data);
        });

        viewModel.ecgDataIII.observe(getViewLifecycleOwner(), data -> {
            binding.ecgViewIII.addECGData(data);
            binding.ecgViewIIIAVR.addECGData(data);
        });

        viewModel.bpm.observe(getViewLifecycleOwner(), bpm -> binding.tvBpm.setText(String.valueOf(bpm)));
        viewModel.recordingTime.observe(getViewLifecycleOwner(), time -> binding.tvRecordingTime.setText(time));
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.ecg_steps);

        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < stepsArray.length; i++) {
            formattedText.append(i + 1).append(". ").append(stepsArray[i]).append("\n");
        }

        binding.tvSteps.setText(formattedText.toString().trim());

        binding.ecgViewI.setMaxY(500);
        binding.ecgViewI.setMinY(100);
        binding.ecgViewI.setStepY(200);

        binding.ecgViewII.setMaxY(500);
        binding.ecgViewII.setMinY(100);
        binding.ecgViewII.setStepY(200);

        binding.ecgViewIII.setMaxY(500);
        binding.ecgViewIII.setMinY(100);
        binding.ecgViewIII.setStepY(200);

        binding.ecgViewIAVR.setMaxY(500);
        binding.ecgViewIAVR.setMinY(100);
        binding.ecgViewIAVR.setStepY(200);

        binding.ecgViewIIAVR.setMaxY(500);
        binding.ecgViewIIAVR.setMinY(100);
        binding.ecgViewIIAVR.setStepY(200);

        binding.ecgViewIIIAVR.setMaxY(500);
        binding.ecgViewIIIAVR.setMinY(100);
        binding.ecgViewIIIAVR.setStepY(200);
    }

    private void setListeners() {
        binding.btnStartHeader.setOnClickListener(v -> {
            viewModel.isHeaderShowed.setValue(true);
        });

        binding.btnStart.setOnClickListener(v -> {
            testViewModel.updateTestItem("ecg", 1, null);
            viewModel.startECGSimulation();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("ecg", 2, viewModel.ecgDataI.getValue() + "," + viewModel.ecgDataII.getValue() + "," + viewModel.ecgDataIII.getValue() + "," + viewModel.bpm.getValue() + "," + viewModel.recordingTime.getValue());
            viewModel.stopECGSimulation();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("ecg", 0, null);
            viewModel.resetECG();
        });
    }

    private void resetState() {
        viewModel.ecgDataI.setValue(0F);
        viewModel.ecgDataII.setValue(0F);
        viewModel.ecgDataIII.setValue(0F);
        viewModel.bpm.setValue(0);
        viewModel.recordingTime.setValue("00:00");
    }
}
