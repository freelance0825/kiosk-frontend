package com.fmv.healthkiosk.ui.home.test.widgets.bloodpressure;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentBloodPressureTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BloodPressureTestFragment extends BaseFragment<FragmentBloodPressureTestBinding, BloodPressureTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<BloodPressureTestViewModel> getViewModelClass() {
        return BloodPressureTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentBloodPressureTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBloodPressureTestBinding.inflate(inflater, container, false);
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
                String oximeterData = testViewModel.selectedTestItem.getValue().getTestResult();

                try {
                    String[] values = oximeterData.split(",");
                    int pulse = Integer.parseInt(values[0].trim());
                    int systolic = Integer.parseInt(values[1].trim());
                    int diastolic = Integer.parseInt(values[2].trim());

                    viewModel.pulseData.setValue(pulse);
                    viewModel.systolicData.setValue(systolic);
                    viewModel.diastolicData.setValue(diastolic);
                } catch (Exception e) {
                    viewModel.pulseData.setValue(0);
                    viewModel.systolicData.setValue(0);
                    viewModel.diastolicData.setValue(0);
                }
            } else {
                viewModel.pulseData.setValue(0);
                viewModel.systolicData.setValue(0);
                viewModel.diastolicData.setValue(0);
            }
        } else {
            viewModel.pulseData.setValue(0);
            viewModel.systolicData.setValue(0);
            viewModel.diastolicData.setValue(0);
        }

        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "blood_pressure")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            String oximeterData = testViewModel.selectedTestItem.getValue().getTestResult();

                            try {
                                String[] values = oximeterData.split(",");
                                int pulse = Integer.parseInt(values[0].trim());
                                int systolic = Integer.parseInt(values[1].trim());
                                int diastolic = Integer.parseInt(values[2].trim());

                                viewModel.pulseData.setValue(pulse);
                                viewModel.systolicData.setValue(systolic);
                                viewModel.diastolicData.setValue(diastolic);
                            } catch (Exception e) {
                                viewModel.pulseData.setValue(0);
                                viewModel.systolicData.setValue(0);
                                viewModel.diastolicData.setValue(0);
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

        viewModel.pulseData.observe(getViewLifecycleOwner(), pulse -> {
            if (pulse != null) {
                binding.customPieProgressPulse.setProgress(pulse);
                binding.tvPulse.setText(String.valueOf(pulse));
            } else {
                binding.customPieProgressPulse.setProgress(0);
                binding.tvPulse.setText(String.valueOf(0));
            }
        });

        viewModel.systolicData.observe(getViewLifecycleOwner(), systolic -> {
            if (systolic != null) {
                binding.customPieProgressSystolic.setProgress(systolic);
                binding.tvSystolic.setText(String.valueOf(systolic));
            } else {
                binding.customPieProgressSystolic.setProgress(0);
                binding.tvSystolic.setText(String.valueOf(0));
            }
        });

        viewModel.diastolicData.observe(getViewLifecycleOwner(), diastolic -> {
            if (diastolic != null) {
                binding.customPieProgressDiastolic.setProgress(diastolic);
                binding.tvDiastolic.setText(String.valueOf(diastolic));
            } else {
                binding.customPieProgressDiastolic.setProgress(0);
                binding.tvDiastolic.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        binding.customPieProgressSystolic.setShowLabels(false);
        binding.customPieProgressDiastolic.setShowLabels(false);

        String[] stepsArray = getResources().getStringArray(R.array.blood_pressure_steps);

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
            testViewModel.updateTestItem("blood_pressure", 1, null);
            viewModel.startUpdatingOximeter();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("blood_pressure", 2, viewModel.pulseData.getValue() + "," + viewModel.systolicData.getValue() + "," + viewModel.diastolicData.getValue());
            viewModel.stopUpdatingOximeter();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("blood_pressure", 0, null);
            viewModel.resetOximeter();
        });
    }
}
