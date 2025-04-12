package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class MakeAppointmentRequest {

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("doctorId")
	private int doctorId;

	@SerializedName("patientId")
	private int patientId;

	@SerializedName("name")
	private String name;

	@SerializedName("healthComplaints")
	private String healthComplaints;


	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHealthComplaints(String healthComplaints) {
		this.healthComplaints = healthComplaints;
	}

	public String getDateTime(){
		return dateTime;
	}

	public int getDoctorId(){
		return doctorId;
	}

	public int getPatientId(){
		return patientId;
	}

	public String getName(){
		return name;
	}

	public String getHealthComplaints(){
		return healthComplaints;
	}
}