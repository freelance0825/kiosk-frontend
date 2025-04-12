package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentRequest implements Parcelable {

    private String dateTime;

    public AppointmentRequest() {}

    protected AppointmentRequest(Parcel in) {
        dateTime = in.readString();
    }

    public static final Creator<AppointmentRequest> CREATOR = new Creator<AppointmentRequest>() {
        @Override
        public AppointmentRequest createFromParcel(Parcel in) {
            return new AppointmentRequest(in);
        }

        @Override
        public AppointmentRequest[] newArray(int size) {
            return new AppointmentRequest[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(dateTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters
    public String getDateTime() {
        return dateTime;
    }

    // Setters
    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
