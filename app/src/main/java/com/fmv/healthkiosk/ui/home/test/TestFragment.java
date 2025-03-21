package com.fmv.healthkiosk.ui.home.test;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentTestBinding;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItemList;
import com.fmv.healthkiosk.ui.home.test.adapters.TestAdapter;
import com.fmv.healthkiosk.ui.home.test.widgets.handler.TestLayoutHandler;

import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TestFragment extends BaseFragment<FragmentTestBinding, TestViewModel> {

    public TestItemList testItemListPackage;
    public MedicalPackage medicalPackage;

    private final TestAdapter testAdapter = new TestAdapter();
    private TestLayoutHandler testLayoutHandler;

    @Override
    protected Class<TestViewModel> getViewModelClass() {
        return TestViewModel.class;
    }

    @Override
    protected void initViewModel(boolean isRequiringActivity) {
        super.initViewModel(true);
    }

    @Override
    protected FragmentTestBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentTestBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        testLayoutHandler = new TestLayoutHandler(getChildFragmentManager(), R.id.layout_test);
        TestFragmentArgs args = TestFragmentArgs.fromBundle(getArguments());

        testItemListPackage = args.getTestItemList();
        medicalPackage = args.getMedicalPackage();

        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.medicalPackage = medicalPackage;
        viewModel.testItemListPackage = testItemListPackage;

        viewModel.testItemList.setValue(testItemListPackage.getTestItemList());
        viewModel.selectTestItem(Objects.requireNonNull(viewModel.testItemList.getValue()).get(0));

        if (viewModel.medicalPackage != null) {
            binding.tvTitle.setText(getString(R.string.fragment_test_package_test, viewModel.medicalPackage.getName()));
        } else {
            binding.tvTitle.setText(getString(R.string.fragment_test_custom_test));
        }

        viewModel.testItemList.observe(getViewLifecycleOwner(), testItems -> {
            if (!testItems.isEmpty()) {
                testAdapter.submitList(testItems);

                boolean allTested = testItems.stream().allMatch(testItem -> testItem.isTested() == 2);
                binding.btnComplete.setVisibility(allTested ? View.VISIBLE : View.GONE);
            }
        });

        viewModel.selectedTestItem.observe(getViewLifecycleOwner(), testItem -> {
            if (testItem != null) testLayoutHandler.updateTestLayout(testItem);
        });
    }

    private void setViews() {
        binding.rvTests.setAdapter(testAdapter);
        binding.rvTests.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        testAdapter.setOnItemClickListener((testItem, position) -> {
            viewModel.selectTestItem(testItem);
            testAdapter.notifyDataSetChanged();
        });
    }
}
