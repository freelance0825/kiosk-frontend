package com.fmv.healthkiosk.core.base.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewbinding.ViewBinding;


public abstract class BaseFragment<VB extends ViewBinding, VM extends BaseViewModel> extends Fragment {

    protected VM viewModel;
    protected VB binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = getViewBinding(inflater, container);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViewModel(false);
        setupUI(savedInstanceState);
    }

    protected void initViewModel(boolean isRequiringActivity) {
        ViewModelProvider provider = isRequiringActivity
                ? new ViewModelProvider(requireActivity())
                : new ViewModelProvider(this);

        viewModel = provider.get(getViewModelClass());
    }

    protected abstract void setupUI(Bundle savedInstanceState);

    protected abstract Class<VM> getViewModelClass();

    protected abstract VB getViewBinding(LayoutInflater inflater, ViewGroup container);

    /**
     * Use when the fragment needs to update the ParentViewModel
     */
    protected <T extends BaseViewModel> T getParentViewModel(Class<T> parentClass) {
        return new ViewModelProvider(requireActivity()).get(parentClass);
    }

    /**
     * Fragment Navigators with Jetpack Navigation
     */
    protected void navigateToFragment(@NonNull NavDirections action, boolean clearBackStack) {
        NavController navController = NavHostFragment.findNavController(this);

        NavOptions.Builder navOptionsBuilder = new NavOptions.Builder();

        if (clearBackStack) {
            navOptionsBuilder.setPopUpTo(navController.getGraph().getStartDestinationId(), true);
        }

        navController.navigate(action, navOptionsBuilder.build());
    }


    protected void navigateBack() {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigateUp();
    }

    /**
     * Activity Navigator
     */
    protected void navigateToActivity(Class<? extends AppCompatActivity> destination, Bundle extras) {
        Intent intent = new Intent(requireContext(), destination);
        if (extras != null) intent.putExtras(extras);
        startActivity(intent);
    }
}
