package com.fmv.healthkiosk.ui.report.prescription;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseActivity;
import com.fmv.healthkiosk.databinding.ActivityPdfPrescriptionReportBinding;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PdfPrescriptionReportActivity extends BaseActivity<ActivityPdfPrescriptionReportBinding, PdfPrescriptionReportViewModel> {

    @Override
    protected Class<PdfPrescriptionReportViewModel> getViewModelClass() {
        return PdfPrescriptionReportViewModel.class;
    }

    @Override
    protected ActivityPdfPrescriptionReportBinding getViewBinding() {
        return ActivityPdfPrescriptionReportBinding.inflate(getLayoutInflater());
    }


    @Override
    protected void setupUI(Bundle savedInstanceState) {

        setListeners();
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
    }
}