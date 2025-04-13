package com.fmv.healthkiosk.ui.report.prescription;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseActivity;
import com.fmv.healthkiosk.core.utils.Base64Helper;
import com.fmv.healthkiosk.databinding.ActivityPdfPrescriptionReportBinding;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.PatientModel;
import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;
import com.fmv.healthkiosk.feature.tests.domain.model.TestResult;
import com.fmv.healthkiosk.ui.report.prescription.adapter.PdfMedicineAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PdfPrescriptionReportActivity extends BaseActivity<ActivityPdfPrescriptionReportBinding, PdfPrescriptionReportViewModel> {

    private final PdfMedicineAdapter pdfMedicineAdapter = new PdfMedicineAdapter();

    public static final String EXTRA_APPOINTMENT_RESULT = "extra_appointment_result";

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
        extractIntentExtras();
        observeViewModel();

        setListeners();
    }

    private void observeViewModel() {
        viewModel.appointmentModel.observe(this, appointmentModel -> {
            if (appointmentModel != null) {
                PatientModel patientModel = appointmentModel.getPatient();
                DoctorModel doctorModel = appointmentModel.getDoctor();

                String genderAge = String.format("%s, %s years old", patientModel.getGender(), patientModel.getAge());

                binding.tvPatientName.setText(patientModel.getName());
                binding.tvPatientGenderAge.setText(genderAge);
                binding.tvPatientAddress.setText(patientModel.getAddress());
                binding.tvPatientMobileNumber.setText(getString(R.string.activity_pdf_prescription_report_mob_no, patientModel.getPhoneNumber()));
                binding.tvPatientId.setText(getString(R.string.activity_pdf_prescription_report_patient_id, String.valueOf(patientModel.getId())));

                binding.tvDateTime.setText(formatDate(appointmentModel.getDateTime()));
                binding.tvDiagnosis.setText(appointmentModel.getPostConsultation().getDiagnosis());
                binding.tvPatientComplaints.setText(appointmentModel.getHealthComplaints());
                binding.tvAdvice.setText(appointmentModel.getPostConsultation().getSuggestions());
                binding.tvFollowUpDate.setText(appointmentModel.getPostConsultation().getFollowUpDate());

                pdfMedicineAdapter.submitList(appointmentModel.getPostConsultation().getMedicines());
                binding.rvMedicines.setAdapter(pdfMedicineAdapter);
                binding.rvMedicines.setLayoutManager(new LinearLayoutManager(this));

                binding.tvDoctorName.setText(doctorModel.getName());
                binding.tvOccupation.setText(doctorModel.getSpecialization());

                if (!appointmentModel.getPostConsultation().getSignature().isEmpty()) {
                    binding.ivSignature.setImageBitmap(Base64Helper.convertToBitmap(appointmentModel.getPostConsultation().getSignature()));
                }
            }
        });
    }

    private void setListeners() {
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void extractIntentExtras() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            AppointmentModel appointmentModel = bundle.getParcelable(EXTRA_APPOINTMENT_RESULT);
            viewModel.setData(appointmentModel);
        }
    }

    public String formatDate(String inputDate) {
        try {
            // Replace 'T' with a space so SimpleDateFormat can parse it
            inputDate = inputDate.replace("T", " ");

            // Define the formats, one with milliseconds and one without
            SimpleDateFormat inputFormatWithMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS", Locale.ENGLISH);
            SimpleDateFormat inputFormatWithoutMillis = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);

            Date date = null;
            try {
                // Try parsing with milliseconds
                date = inputFormatWithMillis.parse(inputDate);
            } catch (ParseException e) {
                // Fallback to parsing without milliseconds if the first format fails
                date = inputFormatWithoutMillis.parse(inputDate);
            }

            // Format the date in the desired output format (for example: "dd MMMM yyyy HH:mm:ss")
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM, yyyy ・hh:mm a", Locale.ENGLISH);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;  // Return null if parsing fails
        }
    }
}