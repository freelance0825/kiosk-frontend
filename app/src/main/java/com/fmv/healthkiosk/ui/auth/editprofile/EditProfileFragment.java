package com.fmv.healthkiosk.ui.auth.editprofile;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseFragment;
import com.fmv.healthkiosk.databinding.FragmentEditProfileBinding;

import java.util.Calendar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class EditProfileFragment extends BaseFragment<FragmentEditProfileBinding, EditProfileViewModel> {

    private Integer userId;

    @Override
    protected Class<EditProfileViewModel> getViewModelClass() {
        return EditProfileViewModel.class;
    }

    @Override
    protected FragmentEditProfileBinding getViewBinding(LayoutInflater inflater, ViewGroup container) {
        return FragmentEditProfileBinding.inflate(inflater, container, false);
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

        viewModel.updateSuccessMessage.observe(getViewLifecycleOwner(), successMessage -> {
            if (successMessage != null) {
                Toast.makeText(requireContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.errorMessage.observe(this, errorMessage -> {
            if (errorMessage != null) {

            }
        });

        // Observe userId and set it when it's available
        viewModel.userId.observe(getViewLifecycleOwner(), userId -> {
            this.userId = userId; // Update userId
        });

        // Observe LiveData fields and update UI accordingly
        viewModel.username.observe(getViewLifecycleOwner(), username -> {
            if (username != null) {
                binding.edName.setText(username);
            }
        });

        viewModel.gender.observe(getViewLifecycleOwner(), gender -> {
            if (gender != null) {
                int genderPosition = ((ArrayAdapter<String>) binding.edGender.getAdapter()).getPosition(gender);
                if (genderPosition >= 0) {
                    binding.edGender.setText(binding.edGender.getAdapter().getItem(genderPosition).toString(), false);
                } else {
                    binding.edGender.setText(gender, false);
                }
            }
        });

        viewModel.phoneNumber.observe(getViewLifecycleOwner(), phoneNumber -> {
            if (phoneNumber != null) {
                // Remove +62 or 62 at the beginning
                String localNumber = phoneNumber.replaceFirst("^\\+?62", "");
                binding.edPhoneNumber.setText(localNumber);
            }
        });

        viewModel.email.observe(getViewLifecycleOwner(), email -> {
            if (email != null) {
                binding.edEmail.setText(email);
            }
        });

        viewModel.dateOfBirth.observe(getViewLifecycleOwner(), dob -> {
            if (dob != null) {
                binding.edDateOfBirth.setText(dob);
            }
        });
    }

    private void setViews() {

        // Set Gender dropdown
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.fragment_register_gender_options));
        binding.edGender.setAdapter(genderAdapter);
        binding.edGender.setOnClickListener(v -> binding.edGender.showDropDown());

        // Set Blood Group dropdown (Static for now)
        ArrayAdapter<String> bloodGroudAdapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.fragment_register_blood_group_options));
        binding.edBloodGroup.setAdapter(bloodGroudAdapter);
        binding.edBloodGroup.setOnClickListener(v -> binding.edBloodGroup.showDropDown());

        // Set Date of Birth
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

        binding.btnSave.setOnClickListener(v -> {
            // Validate and collect the user input
            String name = binding.edName.getText().toString().trim();
            String gender = binding.edGender.getText().toString();
            String phoneNumber = binding.edPhoneNumber.getText().toString().trim();
            String email = binding.edEmail.getText().toString().trim();
            String dob = binding.edDateOfBirth.getText().toString().trim();

            if (isValid(name, gender, phoneNumber, email, dob)) {
                String phoneNumberUpdated = getString(R.string.fragment_login_mobile_number_country_code) + phoneNumber;

                if (userId != null) {
                    // Make the update request with the available userId
                    viewModel.update(userId, name, gender, phoneNumberUpdated, email, dob);
                } else {
                    Toast.makeText(requireContext(), "User ID is not available.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields correctly.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isValid(String name, String gender, String phoneNumber, String email, String dob) {
        if (name.isEmpty()) {
            Toast.makeText(requireContext(), "name", Toast.LENGTH_SHORT).show();
            return false;
        } else if (gender.isEmpty()) {
            Toast.makeText(requireContext(), "gender", Toast.LENGTH_SHORT).show();

            return false;
        } else if (phoneNumber.isEmpty()) {
            Toast.makeText(requireContext(), "phnumb", Toast.LENGTH_SHORT).show();

            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(requireContext(), "name", Toast.LENGTH_SHORT).show();

            return false;
        } else if (dob.isEmpty()) {
            Toast.makeText(requireContext(), "dob", Toast.LENGTH_SHORT).show();

            return false;
        } else {
            return true;
        }
    }

}
