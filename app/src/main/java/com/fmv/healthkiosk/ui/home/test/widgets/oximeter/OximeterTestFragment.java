package com.fmv.healthkiosk.ui.home.test.widgets.oximeter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentOximeterTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class OximeterTestFragment extends BaseFragment<FragmentOximeterTestBinding, OximeterTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<OximeterTestViewModel> getViewModelClass() {
        return OximeterTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentOximeterTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentOximeterTestBinding.inflate(inflater, container, false);
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
                    int oxygen = Integer.parseInt(values[0].trim());
                    int pulse = Integer.parseInt(values[1].trim());

                    viewModel.oxygenData.setValue(oxygen);
                    viewModel.pulseData.setValue(pulse);
                } catch (Exception e) {
                    viewModel.oxygenData.setValue(0);
                    viewModel.pulseData.setValue(0);
                }
            } else {
                viewModel.oxygenData.setValue(0);
                viewModel.pulseData.setValue(0);
            }
        } else {
            viewModel.oxygenData.setValue(0);
            viewModel.pulseData.setValue(0);
        }

        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "oximeter")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            String oximeterData = testViewModel.selectedTestItem.getValue().getTestResult();

                            try {
                                String[] values = oximeterData.split(",");
                                int oxygen = Integer.parseInt(values[0].trim());
                                int pulse = Integer.parseInt(values[1].trim());

                                viewModel.oxygenData.setValue(oxygen);
                                viewModel.pulseData.setValue(pulse);
                            } catch (Exception e) {
                                viewModel.oxygenData.setValue(0);
                                viewModel.pulseData.setValue(0);
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


        viewModel.oxygenData.observe(getViewLifecycleOwner(), oxygen -> {
            if (oxygen != null) {
                binding.customPieProgressSpo2.setProgress(oxygen);
                binding.tvOxygen.setText(String.valueOf(oxygen));
            } else {
                binding.customPieProgressSpo2.setProgress(0);
                binding.tvOxygen.setText(String.valueOf(0));
            }
        });

        viewModel.pulseData.observe(getViewLifecycleOwner(), pulse -> {
            if (pulse != null) {
                binding.customPieProgressPrbpm.setProgress(pulse);
                binding.tvPulse.setText(String.valueOf(pulse));
            } else {
                binding.customPieProgressPrbpm.setProgress(0);
                binding.tvPulse.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.oximeter_steps);

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
            testViewModel.updateTestItem("oximeter", 1, null);
            viewModel.startSimulatedOximeter();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("oximeter", 2, viewModel.oxygenData.getValue() + "," + viewModel.pulseData.getValue());
            viewModel.stopSimulatedOximeter();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("oximeter", 0, null);
            viewModel.resetOximeter();
        });
    }
}
