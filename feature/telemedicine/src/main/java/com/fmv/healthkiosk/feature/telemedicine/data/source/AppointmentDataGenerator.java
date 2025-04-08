package com.fmv.healthkiosk.feature.telemedicine.data.source;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AppointmentDataGenerator {

    public static List<Appointment> generateSampleAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        List<Doctor> doctors = DoctorDataGenerator.generateSampleDoctors();

        long now = System.currentTimeMillis();

        for (int i = 0; i < doctors.size(); i++) {
            Appointment appointment = new Appointment();
            appointment.setId(i);
            appointment.setDoctor(doctors.get(i));

            if (i == 2) {
                // Active appointment with current time
                appointment.setCancelled(false);
                appointment.setRescheduled(false);
                appointment.setDateTime(now);
            } else {
                // Cancelled appointments with past times
                appointment.setCancelled(true);
                appointment.setRescheduled(false);
                appointment.setDateTime(now - (i + 1) * 86400000L); // Past days
            }

            appointments.add(appointment);
        }

        // Sort by dateTime (newest first)
        Collections.sort(appointments, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment a1, Appointment a2) {
                return Long.compare(a2.getDateTime(), a1.getDateTime());
            }
        });

        return appointments;
    }
}
