package com.fmv.healthkiosk.feature.telemedicine.data.source.local;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Notification;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorForNotification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NotificationAppointmentDataGenerator {

    public static List<Notification> generateSampleAppointments() {
        List<Notification> notifications = new ArrayList<>();
        List<DoctorModel> doctorForNotifications = DoctorDataGenerator.generateSampleDoctors();

        long now = System.currentTimeMillis();

        for (int i = 0; i < doctorForNotifications.size(); i++) {
            Notification notification = new Notification();
            notification.setId(i);
            notification.setDoctor(doctorForNotifications.get(i));

            if (i == 2) {
                // Active notification with current time
                notification.setCancelled(false);
                notification.setRescheduled(false);
                notification.setDateTime(now);
            } else {
                // Cancelled notifications with past times
                notification.setCancelled(true);
                notification.setRescheduled(false);
                notification.setDateTime(now - (i + 1) * 86400000L); // Past days
            }

            notifications.add(notification);
        }

        // Sort by dateTime (newest first)
        Collections.sort(notifications, new Comparator<Notification>() {
            @Override
            public int compare(Notification a1, Notification a2) {
                return Long.compare(a2.getDateTime(), a1.getDateTime());
            }
        });

        return notifications;
    }
}
