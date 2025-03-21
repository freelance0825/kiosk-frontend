package com.fmv.healthkiosk.ui.home.test.widgets.height;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentHeightTestBinding;
import com.fmv.healthkiosk.ui.home.test.TestViewModel;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HeightTestFragment extends BaseFragment<FragmentHeightTestBinding, HeightTestViewModel> {

    private TestViewModel testViewModel;

    @Override
    protected Class<HeightTestViewModel> getViewModelClass() {
        return HeightTestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentHeightTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentHeightTestBinding.inflate(inflater, container, false);
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
                viewModel.heightData.setValue(Float.valueOf(testViewModel.selectedTestItem.getValue().getTestResult()));
            } else {
                viewModel.heightData.setValue(0F);
            }
        } else {
            viewModel.heightData.setValue(0F);
        }

        testViewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem == null) return;

            if (Objects.equals(testItem.getId(), "height")) {
                binding.btnStart.setVisibility(testItem.isTested() == 0 ? View.VISIBLE : View.GONE);
                binding.btnStop.setVisibility(testItem.isTested() == 1 ? View.VISIBLE : View.GONE);
                binding.btnRetake.setVisibility(testItem.isTested() == 2 ? View.VISIBLE : View.GONE);

                viewModel.isHeaderShowed.observe(getViewLifecycleOwner(), isHeaderShowed -> {
                    if (testItem.isTested() == 2) {
                        if (!testItem.getTestResult().isEmpty()) {
                            viewModel.heightData.setValue(Float.valueOf(testItem.getTestResult()));
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


        viewModel.heightData.observe(getViewLifecycleOwner(), height -> {
            if (height != null) {
                binding.heightScaleView.setSelectedValue(height);
                binding.tvHeight.setText(String.valueOf(height));
            } else {
                binding.heightScaleView.setSelectedValue(0);
                binding.tvHeight.setText(String.valueOf(0));
            }
        });
    }

    private void setViews() {
        String[] stepsArray = getResources().getStringArray(R.array.height_steps);

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
            testViewModel.updateTestItem("height", 1, null);
            viewModel.startUpdatingHeight();
        });

        binding.btnStop.setOnClickListener(v -> {
            testViewModel.updateTestItem("height", 2, Objects.requireNonNull(viewModel.heightData.getValue()).toString());
            viewModel.stopUpdatingHeight();
        });

        binding.btnRetake.setOnClickListener(v -> {
            testViewModel.updateTestItem("height", 0, null);
            viewModel.resetHeight();
        });
    }
}
