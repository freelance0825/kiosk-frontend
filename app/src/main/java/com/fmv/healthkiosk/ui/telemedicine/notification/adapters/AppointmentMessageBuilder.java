package com.fmv.healthkiosk.ui.telemedicine.notification.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AppointmentMessageBuilder {

    public static String getNowMessage(String userName, long dateTimeMillis, String doctorName) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh.mm a", Locale.getDefault());
        String time = timeFormat.format(new Date(dateTimeMillis));

        return "Hi, " + userName + ". Your appointment at " + time +
                " with " + doctorName + " is happening now. Don’t let your doctor waiting for long time.";
    }

    public static String getCancelledMessage(String userName, long dateTimeMillis, String doctorName, String specialization) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh.mm a", Locale.getDefault());
        String dateTime = dateFormat.format(new Date(dateTimeMillis));

        return "Hi, " + userName + ". Your appointment at " + dateTime +
                " with " + doctorName + ", " + specialization + " has been cancelled.";
    }

    public static String getRescheduledMessage(String userName, long originalDateTime, long newDateTime, String doctorName) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh.mm a", Locale.getDefault());

        String originalDate = dateFormat.format(new Date(originalDateTime));
        String originalTime = timeFormat.format(new Date(originalDateTime));
        String newTime = timeFormat.format(new Date(newDateTime));

        return "Hi, " + userName + ". Your appointment at " + originalDate + ", " + originalTime +
                " with " + doctorName + " was going to be rescheduled to " +
                originalDate + ", " + newTime + " . Confirm with your doctor now";
    }
}
