package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Appointment implements Parcelable {

    private int id;
    private boolean isCancelled;
    private boolean isRescheduled;
    private long dateTime;
    private Doctor doctor;

    public Appointment() {
    }

    protected Appointment(Parcel in) {
        id = in.readInt();
        isCancelled = in.readByte() != 0;
        isRescheduled = in.readByte() != 0;
        dateTime = in.readLong();
        doctor = in.readParcelable(Doctor.class.getClassLoader());
    }

    public static final Creator<Appointment> CREATOR = new Creator<Appointment>() {
        @Override
        public Appointment createFromParcel(Parcel in) {
            return new Appointment(in);
        }

        @Override
        public Appointment[] newArray(int size) {
            return new Appointment[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (isCancelled ? 1 : 0));
        dest.writeByte((byte) (isRescheduled ? 1 : 0));
        dest.writeLong(dateTime);
        dest.writeParcelable(doctor, flags);
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

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }


    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
