package com.fmv.healthkiosk.ui.home.test.widgets.bloodsugar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentBloodSugarTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BloodSugarTestFragment extends BaseFragment<FragmentBloodSugarTestBinding, BloodSugarTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<BloodSugarTestViewModel> getViewModelClass() {
        return BloodSugarTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentBloodSugarTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentBloodSugarTestBinding.inflate(inflater, container, false);
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
                viewModel.bloodSugarData.setValue(Float.valueOf(testViewModel.selectedTestItem.getValue().getTestResult()));
            } else {
                viewModel.bloodSugarData.setValue(0F);
            }
        } else {
            viewModel.bloodSugarData.setValue(0F);
        }

        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "blood_sugar")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            viewModel.bloodSugarData.setValue(Float.valueOf(testItem.getTestResult()));
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


        viewModel.bloodSugarData.observe(getViewLifecycleOwner(), bloodSugar -> {
            if (bloodSugar != null) {
                float maxValue = 100F;
                float percentage = (bloodSugar / maxValue) * 100F;

                binding.speedometerView.setValue(Math.min(percentage, 100F));
                binding.tvBloodSugar.setText(String.valueOf(bloodSugar));
            } else {
                binding.speedometerView.setValue(0F);
                binding.tvBloodSugar.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.blood_sugar_steps);

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
            testViewModel.updateTestItem("blood_sugar", 1, null);
            viewModel.startUpdatingBloodSugar();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("blood_sugar", 2, Objects.requireNonNull(viewModel.bloodSugarData.getValue()).toString());
            viewModel.stopUpdatingBloodSugar();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("blood_sugar", 0, null);
            viewModel.resetBloodSugar();
        });
    }
}
