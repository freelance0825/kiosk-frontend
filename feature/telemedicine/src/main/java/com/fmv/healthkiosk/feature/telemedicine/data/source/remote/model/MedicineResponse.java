package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class MedicineResponse{

	@SerializedName("afternoon")
	private String afternoon;

	@SerializedName("duration")
	private String duration;

	@SerializedName("imageBase64")
	private String imageBase64;

	@SerializedName("notes")
	private String notes;

	@SerializedName("night")
	private String night;

	@SerializedName("name")
	private String name;

	@SerializedName("postConsultationId")
	private int postConsultationId;

	@SerializedName("morning")
	private String morning;

	public String getAfternoon(){
		return afternoon;
	}

	public String getDuration(){
		return duration;
	}

	public String getImageBase64(){
		return imageBase64;
	}

	public String getNotes(){
		return notes;
	}

	public String getNight(){
		return night;
	}

	public String getName(){
		return name;
	}

	public int getPostConsultationId(){
		return postConsultationId;
	}

	public String getMorning(){
		return morning;
	}
}