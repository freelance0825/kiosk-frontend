package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DoctorTimeslotModel implements Parcelable {

	private String date;
	private List<String> availableTimeSlots;

	public DoctorTimeslotModel() {
		// Default constructor
	}

	protected DoctorTimeslotModel(Parcel in) {
		date = in.readString();
		availableTimeSlots = new ArrayList<>();
		in.readStringList(availableTimeSlots);
	}

	public static final Creator<DoctorTimeslotModel> CREATOR = new Creator<DoctorTimeslotModel>() {
		@Override
		public DoctorTimeslotModel createFromParcel(Parcel in) {
			return new DoctorTimeslotModel(in);
		}

		@Override
		public DoctorTimeslotModel[] newArray(int size) {
			return new DoctorTimeslotModel[size];
		}
	};

	public String getDate() {
		return date;
	}

	public List<String> getAvailableTimeSlots() {
		return availableTimeSlots;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setAvailableTimeSlots(List<String> availableTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeStringList(availableTimeSlots);
	}
}
