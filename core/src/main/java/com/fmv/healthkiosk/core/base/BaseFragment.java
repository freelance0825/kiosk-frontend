package com.fmv.healthkiosk.core.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
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
        viewModel = new ViewModelProvider(this).get(getViewModelClass());

        setupUI(savedInstanceState);
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
    protected void navigateToFragment(@IdRes int actionId, Bundle args) {
        NavController navController = NavHostFragment.findNavController(this);
        navController.navigate(actionId, args);
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
