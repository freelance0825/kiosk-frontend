package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DoctorTimeslotModel implements Parcelable {

	private String date;
	private List<TimeslotModel> availableTimeSlots;

	public DoctorTimeslotModel() {
		// Default constructor
	}

	protected DoctorTimeslotModel(Parcel in) {
		date = in.readString();
		availableTimeSlots = in.createTypedArrayList(TimeslotModel.CREATOR);
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

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(date);
		dest.writeTypedList(availableTimeSlots);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public String getDate() {
		return date;
	}

	public List<TimeslotModel> getAvailableTimeSlots() {
		return availableTimeSlots;
	}

	// Setters
	public void setDate(String date) {
		this.date = date;
	}

	public void setAvailableTimeSlots(List<TimeslotModel> availableTimeSlots) {
		this.availableTimeSlots = availableTimeSlots;
	}
}
