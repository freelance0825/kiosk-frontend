package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TimeslotModel implements Parcelable {
	private boolean available;
	private String time;

	// Default constructor
	public TimeslotModel() {}

	// Constructor from Parcel
	protected TimeslotModel(Parcel in) {
		available = in.readByte() != 0;
		time = in.readString();
	}

	public static final Creator<TimeslotModel> CREATOR = new Creator<TimeslotModel>() {
		@Override
		public TimeslotModel createFromParcel(Parcel in) {
			return new TimeslotModel(in);
		}

		@Override
		public TimeslotModel[] newArray(int size) {
			return new TimeslotModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (available ? 1 : 0));
		dest.writeString(time);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public boolean isAvailable() {
		return available;
	}

	public String getTime() {
		return time;
	}

	// Setters
	public void setAvailable(boolean available) {
		this.available = available;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
