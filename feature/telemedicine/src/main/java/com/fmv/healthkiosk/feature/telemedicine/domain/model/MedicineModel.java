package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicineModel implements Parcelable {

	private String afternoon;
	private String duration;
	private String imageBase64;
	private String notes;
	private String night;
	private String name;
	private int postConsultationId;
	private String morning;

	// Constructor
	public MedicineModel() {}

	// Parcelable constructor
	protected MedicineModel(Parcel in) {
		afternoon = in.readString();
		duration = in.readString();
		imageBase64 = in.readString();
		notes = in.readString();
		night = in.readString();
		name = in.readString();
		postConsultationId = in.readInt();
		morning = in.readString();
	}

	public static final Creator<MedicineModel> CREATOR = new Creator<MedicineModel>() {
		@Override
		public MedicineModel createFromParcel(Parcel in) {
			return new MedicineModel(in);
		}

		@Override
		public MedicineModel[] newArray(int size) {
			return new MedicineModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(afternoon);
		dest.writeString(duration);
		dest.writeString(imageBase64);
		dest.writeString(notes);
		dest.writeString(night);
		dest.writeString(name);
		dest.writeInt(postConsultationId);
		dest.writeString(morning);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public String getAfternoon() {
		return afternoon;
	}

	public String getDuration() {
		return duration;
	}

	public String getImageBase64() {
		return imageBase64;
	}

	public String getNotes() {
		return notes;
	}

	public String getNight() {
		return night;
	}

	public String getName() {
		return name;
	}

	public int getPostConsultationId() {
		return postConsultationId;
	}

	public String getMorning() {
		return morning;
	}

	// Setters
	public void setAfternoon(String afternoon) {
		this.afternoon = afternoon;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setNight(String night) {
		this.night = night;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPostConsultationId(int postConsultationId) {
		this.postConsultationId = postConsultationId;
	}

	public void setMorning(String morning) {
		this.morning = morning;
	}
}
