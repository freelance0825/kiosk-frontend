package com.fmv.healthkiosk.core.base;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewbinding.ViewBinding;

public abstract class BaseActivity<VB extends ViewBinding, VM extends BaseViewModel> extends AppCompatActivity {

    protected VM viewModel;
    protected VB binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewBinding();
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(getViewModelClass());

        setupUI(savedInstanceState);
    }

    protected abstract void setupUI(Bundle savedInstanceState);

    protected abstract Class<VM> getViewModelClass();

    protected abstract VB getViewBinding();

    protected void navigateToActivity(Class<? extends AppCompatActivity> destination, Bundle extras) {
        Intent intent = new Intent(this, destination);
        if (extras != null) intent.putExtras(extras);
        startActivity(intent);
    }

    protected void addFragment(
            int fragmentContainer,
            Fragment destinationFragment,
            boolean addToBackStack
    ) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .add(fragmentContainer, destinationFragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}
