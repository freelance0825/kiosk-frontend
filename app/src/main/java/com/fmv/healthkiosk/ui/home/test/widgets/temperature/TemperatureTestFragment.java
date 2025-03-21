package com.fmv.healthkiosk.ui.home.test.widgets.temperature;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.fragment.NavHostFragment;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentTemperatureTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TemperatureTestFragment extends BaseFragment<FragmentTemperatureTestBinding, TemperatureTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<TemperatureTestViewModel> getViewModelClass() {
        return TemperatureTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentTemperatureTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTemperatureTestBinding.inflate(inflater, container, false);
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
                viewModel.temperatureData.setValue(Float.valueOf(testViewModel.selectedTestItem.getValue().getTestResult()));
            } else {
                viewModel.temperatureData.setValue(0F);
            }
        } else {
            viewModel.temperatureData.setValue(0F);
        }

        viewModel.selectedTemperatureType.observe(getViewLifecycleOwner(), this::updateWeightSelection);
        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "temperature")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            viewModel.temperatureData.setValue(Float.valueOf(testItem.getTestResult()));
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


        viewModel.temperatureData.observe(getViewLifecycleOwner(), temperature -> {
            if (temperature != null) {
                float maxValue = 100F;
                float percentage = (temperature / maxValue) * 100F;

                binding.speedometerView.setValue(Math.min(percentage, 100F));
                binding.tvTemperature.setText(String.valueOf(temperature));
            } else {
                binding.speedometerView.setValue(0F);
                binding.tvTemperature.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.temperature_steps);

        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < stepsArray.length; i++) {
            formattedText.append(i + 1).append(". ").append(stepsArray[i]).append("\n");
        }

        binding.tvSteps.setText(formattedText.toString().trim());
    }

    private void setListeners() {
        binding.btnFahrenheit.setOnClickListener(v -> {
            viewModel.selectedTemperatureType.setValue(ContextCompat.getString(requireContext(), R.string.fragment_temperature_test_fahrenheit));
        });

        binding.btnCelcius.setOnClickListener(v -> {
            viewModel.selectedTemperatureType.setValue(ContextCompat.getString(requireContext(), R.string.fragment_temperature_test_celcius));
        });

        binding.btnStartHeader.setOnClickListener(v -> {
            viewModel.isHeaderShowed.setValue(true);
        });

        binding.btnStart.setOnClickListener(v -> {
            testViewModel.updateTestItem("temperature", 1, null);
            viewModel.startUpdatingTemperature();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("temperature", 2, Objects.requireNonNull(viewModel.temperatureData.getValue()).toString());
            viewModel.stopUpdatingTemperature();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("temperature", 0, null);
            viewModel.resetTemperature();
        });
    }

    private void updateWeightSelection(String selectedWeightType) {
        boolean isFahrenheitSelected = "°F".equals(selectedWeightType);

        binding.btnFahrenheit.setTextColor(ContextCompat.getColor(requireContext(),
                isFahrenheitSelected ? R.color.onWhiteSurface : R.color.white));
        binding.btnCelcius.setTextColor(ContextCompat.getColor(requireContext(),
                isFahrenheitSelected ? R.color.white : R.color.onWhiteSurface));

        binding.btnFahrenheit.setBackgroundResource(isFahrenheitSelected ? R.drawable.bg_rounded_white_border : 0);
        binding.btnFahrenheit.setBackgroundTintList(isFahrenheitSelected ? ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryGreen)) : null);

        binding.btnCelcius.setBackgroundResource(isFahrenheitSelected ? 0 : R.drawable.bg_rounded_white_border);
        binding.btnCelcius.setBackgroundTintList(isFahrenheitSelected ? null : ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryGreen)));

        binding.tvTemperatureType.setText(selectedWeightType);
    }
}
