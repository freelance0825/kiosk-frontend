package com.fmv.healthkiosk.feature.telemedicine.data.source;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class DoctorDataGenerator {

    public static List<Doctor> generateSampleDoctors() {
        List<Doctor> doctors = new ArrayList<>();

        String[] names = {"Dr. Alice Morgan", "Dr. Brian Hart", "Dr. Clara Evans", "Dr. David Zhou", "Dr. Evelyn Nash"};
        String[] specializations = {"Cardiologist", "Neurologist", "Dermatologist", "Pediatrician", "Orthopedic Surgeon"};

        long currentTimeMillis = System.currentTimeMillis();
        long oneDayMillis = 24 * 60 * 60 * 1000;
        Random random = new Random();

        for (int i = 0; i < 5; i++) {
            Doctor doctor = new Doctor();
            doctor.setId(i);
            doctor.setName(names[i]);
            doctor.setSpecialization(specializations[i]);
            doctor.setImageBase64(""); // empty string
            doctor.setStatus("Available");

            // Generate review between 4.0 to 5.0 with 1 decimal place
            float review = 4.0f + random.nextFloat(); // 4.0 to <5.0
            doctor.setReview(Math.round(review * 10) / 10.0f); // round to 1 decimal place

            doctor.setEmail("doctor" + i + "@example.com");
            doctor.setPassword("password" + i);
            doctor.setState("State " + (i + 1));
            doctor.setClinic("Clinic " + (i + 1));
            doctor.setGender(i % 2 == 0 ? "Male" : "Female");
            doctor.setAge(String.valueOf(35 + i));
            doctor.setDob("1990-01-0" + (i + 1));

            long dateTime;
            if (i < 4) {
                dateTime = currentTimeMillis - ((2 + i) * oneDayMillis); // 2–5 days ago
            } else {
                dateTime = currentTimeMillis; // now
            }
            doctor.setDateTime(dateTime);

            // Randomize isLive flag
            doctor.setLive(random.nextBoolean());

            doctors.add(doctor);
        }

        // Sort by newest dateTime first
        Collections.sort(doctors, new Comparator<Doctor>() {
            @Override
            public int compare(Doctor d1, Doctor d2) {
                return Long.compare(d2.getDateTime(), d1.getDateTime());
            }
        });

        return doctors;
    }
}
