package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

public class TimeslotResponse{
	private boolean available;
	private String time;

	public boolean isAvailable(){
		return available;
	}

	public String getTime(){
		return time;
	}
}
