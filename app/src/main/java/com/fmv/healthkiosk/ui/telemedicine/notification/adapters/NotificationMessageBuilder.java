package com.fmv.healthkiosk.ui.telemedicine.notification.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationMessageBuilder {

    public static String getNowMessage(String userName, String date, String doctorName) {
        return "Hi, " + userName + ". Your appointment at " + date +
                " with " + doctorName + " is happening now. Don’t let your doctor waiting for long time.";
    }

    public static String getCancelledMessage(String userName, String dateTime, String doctorName, String specialization) {
        return "Hi, " + userName + ". Your appointment at " + dateTime +
                " with " + doctorName + ", " + specialization + " has been cancelled.";
    }

    public static String getRescheduledMessage(String userName, String newDateTime, String doctorName) {
        return "Hi, " + userName + ". Your appointment at with " + doctorName + " was going to be rescheduled to " +
                newDateTime + ".";
    }

    public static String getBookedMessage(String userName, String newDateTime, String doctorName) {
        return "Hi, " + userName + ". Your booked a new appointment with " + doctorName + " at " +
                newDateTime + "!";
    }
}
