package com.fmv.healthkiosk.ui.auth.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentRegisterBinding;

import java.util.Calendar;

public class RegisterFragment extends BaseFragment<FragmentRegisterBinding, RegisterViewModel> {

    @Override
    protected Class<RegisterViewModel> getViewModelClass() {
        return RegisterViewModel.class;
    }

    @Override
    protected FragmentRegisterBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentRegisterBinding.inflate(inflater, container, false);
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        setViews();
        setListeners();
    }

    private void setViews() {

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.fragment_register_gender_options));
        binding.edGender.setAdapter(genderAdapter);
        binding.edGender.setOnClickListener(v -> binding.edGender.showDropDown());

        ArrayAdapter<String> bloodGroudAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.fragment_register_blood_group_options));
        binding.edBloodGroup.setAdapter(bloodGroudAdapter);
        binding.edBloodGroup.setOnClickListener(v -> binding.edBloodGroup.showDropDown());

        binding.edDateOfBirth.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear);
                        binding.edDateOfBirth.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());

        binding.btnSubmit.setOnClickListener(v -> {
            boolean isCreatingPin = true;

            navigateToFragment(RegisterFragmentDirections.actionNavigationRegisterToNavigationPin(isCreatingPin), false);
        });
    }
}
