package com.fmv.healthkiosk.ui.auth.register;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentRegisterBinding;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
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
        observeViewModel();

        setViews();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isLoading.observe(this, isLoading -> {

        });

        viewModel.registerSuccessMessage.observe(this, successMessage -> {
            if (successMessage != null) {
                navigateToPin();
            }
        });

        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {

            }
        });
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
                        viewModel.updateAge(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> navigateBack());


        binding.btnSubmit.setOnClickListener(v -> {
//            Toast.makeText(requireContext(), "CLICKED", Toast.LENGTH_SHORT).show();

            String name = binding.edName.getText().toString().trim();
            String gender = binding.edGender.getText().toString();
            String dob = binding.edDateOfBirth.getText().toString().trim();
            String phoneNumber = binding.edPhoneNumber.getText().toString().trim();
            String email = binding.edEmail.getText().toString().trim();

            if (isValid(name, gender, dob, phoneNumber, email)) {
                String phoneNumberUpdated = getString(R.string.fragment_login_mobile_number_country_code) + binding.edPhoneNumber.getText().toString().trim();

                viewModel.register(name, gender, dob, phoneNumberUpdated);
            } else {
                Toast.makeText(requireContext(), "EYOW", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid(String name, String gender, String dob, String phoneNumber, String email) {
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (gender.isEmpty()) {
            Toast.makeText(requireContext(), "gender", Toast.LENGTH_SHORT).show();

            return false;
        } else if (dob.isEmpty()) {
            Toast.makeText(requireContext(), "dob", Toast.LENGTH_SHORT).show();

            return false;
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "phnumb", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "name", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }

    private void navigateToPin() {
        boolean isCreatingPin = true;
        navigateToFragment(RegisterFragmentDirections.actionNavigationRegisterToNavigationPin(isCreatingPin), false);
    }
}
