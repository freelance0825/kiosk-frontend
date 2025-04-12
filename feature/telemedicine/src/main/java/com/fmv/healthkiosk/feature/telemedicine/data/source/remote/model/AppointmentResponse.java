package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class AppointmentResponse{

	@SerializedName("doctor")
	private DoctorResponse doctor;

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("imageBase64")
	private String imageBase64;

	@SerializedName("patient")
	private PatientResponse patientResponse;

	@SerializedName("specialization")
	private String specialization;

	@SerializedName("healthComplaints")
	private String healthComplaints;

	@SerializedName("id")
	private int id;

	@SerializedName("postConsultation")
	private PostConsultationResponse postConsultationResponse;

	public DoctorResponse getDoctor(){
		return doctor;
	}

	public String getDateTime(){
		return dateTime;
	}

	public String getImageBase64(){
		return imageBase64;
	}

	public PatientResponse getPatient(){
		return patientResponse;
	}

	public String getSpecialization(){
		return specialization;
	}

	public String getHealthComplaints(){
		return healthComplaints;
	}

	public int getId(){
		return id;
	}

	public PostConsultationResponse getPostConsultation(){
		return postConsultationResponse;
	}
}