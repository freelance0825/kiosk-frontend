package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PostConsultationResponse {

	@SerializedName("dateTime")
	private String dateTime;

	@SerializedName("followUpDate")
	private String followUpDate;

	@SerializedName("signature")
	private String signature;

	@SerializedName("diagnosis")
	private String diagnosis;

	@SerializedName("suggestions")
	private String suggestions;

	@SerializedName("medicines")
	private List<MedicineResponse> medicineResponses;

	public List<MedicineResponse> getMedicineResponses() {
		return medicineResponses;
	}

	@SerializedName("id")
	private int id;

	public String getDateTime(){
		return dateTime;
	}

	public String getFollowUpDate(){
		return followUpDate;
	}

	public String getSignature(){
		return signature;
	}

	public String getDiagnosis(){
		return diagnosis;
	}

	public String getSuggestions(){
		return suggestions;
	}

	public int getId(){
		return id;
	}
}