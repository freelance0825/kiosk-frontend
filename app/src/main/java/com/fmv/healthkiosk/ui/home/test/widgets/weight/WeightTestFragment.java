package com.fmv.healthkiosk.ui.home.test.widgets.weight;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentTestBinding;
import com.fmv.healthkiosk.databinding.FragmentWeightTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;
import com.fmv.healthkiosk.ui.home.test.adapters.TestAdapter;
import com.fmv.healthkiosk.ui.home.test.widgets.handler.TestLayoutHandler;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WeightTestFragment extends BaseFragment<FragmentWeightTestBinding, WeightTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<WeightTestViewModel> getViewModelClass() {
        return WeightTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentWeightTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentWeightTestBinding.inflate(inflater, container, false);
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
                viewModel.weightData.setValue(Float.valueOf(testViewModel.selectedTestItem.getValue().getTestResult()));
            } else {
                viewModel.weightData.setValue(0F);
            }
        } else {
            viewModel.weightData.setValue(0F);
        }

        viewModel.selectedWeightType.observe(getViewLifecycleOwner(), this::updateWeightSelection);
        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "weight")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {

                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            viewModel.weightData.setValue(Float.valueOf(testItem.getTestResult()));
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


        viewModel.weightData.observe(getViewLifecycleOwner(), weight -> {
            if (weight != null) {
                float maxValue = 350F;
                float percentage = (weight / maxValue) * 100F;

                binding.speedometerView.setValue(Math.min(percentage, 100F));
                binding.tvWeight.setText(String.valueOf(weight));
            } else {
                binding.speedometerView.setValue(0F);
                binding.tvWeight.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.weight_steps);

        StringBuilder formattedText = new StringBuilder();
        for (int i = 0; i < stepsArray.length; i++) {
            formattedText.append(i + 1).append(". ").append(stepsArray[i]).append("\n");
        }

        binding.tvSteps.setText(formattedText.toString().trim());
    }

    private void setListeners() {
        binding.btnKgs.setOnClickListener(v -> {
            viewModel.selectedWeightType.setValue(ContextCompat.getString(requireContext(), R.string.fragment_weight_kg));
        });

        binding.btnLbs.setOnClickListener(v -> {
            viewModel.selectedWeightType.setValue(ContextCompat.getString(requireContext(), R.string.fragment_weight_lbs));
        });

        binding.btnStartHeader.setOnClickListener(v -> {
            viewModel.isHeaderShowed.setValue(true);
            viewModel.startScanning();
        });


        binding.btnStart.setOnClickListener(v -> {
            testViewModel.updateTestItem("weight", 1, null);
            viewModel.startMeasuring();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("weight", 2, Objects.requireNonNull(viewModel.weightData.getValue()).toString());
            viewModel.stopMeasuring();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("weight", 0, null);
            viewModel.resetWeight();
        });

//        binding.btnStart.setOnClickListener(v -> {
//            testViewModel.updateTestItem("weight", 1, null);
//            viewModel.startUpdatingWeight();
//        });
//
//        binding.btnStop.setOnClickListener(v -> {
//            testViewModel.updateTestItem("weight", 2, Objects.requireNonNull(viewModel.weightData.getValue()).toString());
//            viewModel.stopUpdatingWeight();
//        });
//
//        binding.btnRetake.setOnClickListener(v -> {
//            testViewModel.updateTestItem("weight", 0, null);
//            viewModel.resetWeight();
//        });
    }

    private void updateWeightSelection(String selectedWeightType) {
        boolean isKgSelected = "KG".equals(selectedWeightType);

        binding.btnKgs.setTextColor(ContextCompat.getColor(requireContext(),
                isKgSelected ? R.color.onWhiteSurface : R.color.white));
        binding.btnLbs.setTextColor(ContextCompat.getColor(requireContext(),
                isKgSelected ? R.color.white : R.color.onWhiteSurface));

        binding.btnKgs.setBackgroundResource(isKgSelected ? R.drawable.bg_rounded_white_border : 0);
        binding.btnKgs.setBackgroundTintList(isKgSelected ? ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryGreen)) : null);

        binding.btnLbs.setBackgroundResource(isKgSelected ? 0 : R.drawable.bg_rounded_white_border);
        binding.btnLbs.setBackgroundTintList(isKgSelected ? null : ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.primaryGreen)));

        binding.tvWeightType.setText(selectedWeightType);
    }
}
