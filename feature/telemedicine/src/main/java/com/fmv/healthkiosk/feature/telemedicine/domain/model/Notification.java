package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Notification implements Parcelable {

    private int id;
    private boolean isCancelled;
    private boolean isRescheduled;
    private boolean isBooked;
    private long dateTime;
    private DoctorModel doctorForNotification;

    public Notification() {
    }

    protected Notification(Parcel in) {
        id = in.readInt();
        isCancelled = in.readByte() != 0;
        isRescheduled = in.readByte() != 0;
        dateTime = in.readLong();
        doctorForNotification = in.readParcelable(DoctorModel.class.getClassLoader());
        isBooked = in.readByte() != 0; // Read the isBooked field
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isCancelled ? 1 : 0));
        dest.writeByte((byte) (isRescheduled ? 1 : 0));
        dest.writeLong(dateTime);
        dest.writeParcelable(doctorForNotification, flags);
        dest.writeByte((byte) (isBooked ? 1 : 0)); // Write the isBooked field
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public boolean isRescheduled() {
        return isRescheduled;
    }

    public void setRescheduled(boolean rescheduled) {
        isRescheduled = rescheduled;
    }

    public DoctorModel getDoctor() {
        return doctorForNotification;
    }

    public void setDoctor(DoctorModel doctorForNotification) {
        this.doctorForNotification = doctorForNotification;
    }


    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}
