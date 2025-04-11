package com.fmv.healthkiosk.feature.telemedicine.data.source.local;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorForNotification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.Collections;
import java.util.Comparator;

public class DoctorDataGenerator {

    public static List<DoctorModel> generateSampleDoctors() {
        List<DoctorModel> doctorList = new ArrayList<>();

        String[] names = {
                "Dr. Alice Morgan", "Dr. Brian Hart", "Dr. Clara Evans",
                "Dr. David Zhou", "Dr. Evelyn Nash"
        };
        String[] specializations = {
                "Cardiologist", "Neurologist", "Dermatologist",
                "Pediatrician", "Orthopedic Surgeon"
        };

        Random random = new Random();

        for (int i = 0; i < names.length; i++) {
            DoctorModel doctor = new DoctorModel();
            doctor.setId(i);
            doctor.setName(names[i]);
            doctor.setSpecialization(specializations[i]);
            doctor.setImageBase64(""); // Placeholder image
            doctor.setStatus("Available");

            // Generate integer review between 4 and 5
            int review = 4 + random.nextInt(2); // 4 or 5
            doctor.setReview(review);

            doctor.setEmail("doctor" + i + "@example.com");
            doctor.setPassword("password" + i);
            doctor.setState("State " + (i + 1));
            doctor.setClinic("Clinic " + (i + 1));
            doctor.setGender(i % 2 == 0 ? "Male" : "Female");
            doctor.setAge(String.valueOf(35 + i));
            doctor.setDob("1990-01-0" + (i + 1));

            doctorList.add(doctor);
        }

        return doctorList;
    }
}
