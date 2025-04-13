package com.fmv.healthkiosk.ui;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static androidx.navigation.ActivityKt.findNavController;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.facebook.react.modules.core.PermissionListener;
import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseActivity;
import com.fmv.healthkiosk.databinding.ActivityMainBinding;

import org.jitsi.meet.sdk.JitsiMeetActivityDelegate;
import org.jitsi.meet.sdk.JitsiMeetActivityInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements JitsiMeetActivityInterface {
    @Override
    protected Class<MainViewModel> getViewModelClass() {
        return MainViewModel.class;
    }

    @Override
    protected ActivityMainBinding getViewBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setupUI(Bundle savedInstanceState) {
        observeViewModel();
        setListeners();
    }

    private void observeViewModel() {
        viewModel.isLoggedIn.observe(this, isLoggedIn -> {
            binding.layoutProfile.setVisibility(isLoggedIn ? View.VISIBLE : View.GONE);

            if (!isLoggedIn) {
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_landing);
            }
        });

        viewModel.username.observe(this, username -> {
            binding.tvPatientName.setText(username);
        });

        viewModel.dateOfBirth.observe(this, dateOfBirth -> {
            binding.tvPatientDateOfBirth.setText(dateOfBirth);
        });

        viewModel.gender.observe(this, gender -> {
            binding.tvPatientGender.setText(gender);
        });

        viewModel.phoneNumber.observe(this, phoneNumber -> {
            binding.tvPatientPhone.setText(phoneNumber);
        });

        viewModel.age.observe(this, age -> {
            binding.tvPatientAge.setText(String.valueOf(age));
        });
    }

    private void setListeners() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            boolean isTestFragment = destination.getId() == R.id.navigation_test;

            binding.layoutDeviceInfo.setVisibility(isTestFragment ? View.VISIBLE : View.GONE);
            binding.btnMyProfile.setVisibility(isTestFragment ? View.GONE : View.VISIBLE);
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment);

                if (navController.getCurrentDestination() != null && navController.popBackStack()) {
                    return;
                }

                setEnabled(false);
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        binding.btnMyProfile.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, v); // or getContext() if inside Fragment
            popupMenu.getMenuInflater().inflate(R.menu.profile_dropdown_menu, popupMenu.getMenu());

            // Force icons to show using reflection
            try {
                Field[] fields = popupMenu.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popupMenu);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupMenu.setOnMenuItemClickListener(item -> {
                int id = item.getItemId();
//                if (id == R.id.menu_edit_profile) {
//                    // Handle Edit My Profile
//                    NavController navControllerEdit = Navigation.findNavController(this, R.id.nav_host_fragment);
//                    navControllerEdit.navigate(R.id.navigation_edit_profile);
//                    return true;
//                } else
                if (id == R.id.menu_logout) {
                    // Handle Logout
                    viewModel.logout();
                    return true;
                }
                return false;
            });

            popupMenu.show();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        JitsiMeetActivityDelegate.onHostResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JitsiMeetActivityDelegate.onHostPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        JitsiMeetActivityDelegate.onNewIntent(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        JitsiMeetActivityDelegate.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        JitsiMeetActivityDelegate.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        JitsiMeetActivityDelegate.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) {
        requestPermissions(strings, i);
    }
}
