package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DoctorTimeslotResponse{

	@SerializedName("date")
	private String date;

	@SerializedName("availableTimeSlots")
	private List<TimeslotResponse> availableTimeSlots;

	public String getDate(){
		return date;
	}

	public List<TimeslotResponse> getAvailableTimeSlots(){
		return availableTimeSlots;
	}
}