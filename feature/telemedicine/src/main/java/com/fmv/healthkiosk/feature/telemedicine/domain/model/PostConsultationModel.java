package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PostConsultationModel implements Parcelable {

	private String dateTime;
	private String followUpDate;
	private String signature;
	private String diagnosis;
	private String suggestions;
	private int id;

	public PostConsultationModel() {}

	protected PostConsultationModel(Parcel in) {
		dateTime = in.readString();
		followUpDate = in.readString();
		signature = in.readString();
		diagnosis = in.readString();
		suggestions = in.readString();
		id = in.readInt();
	}

	public static final Creator<PostConsultationModel> CREATOR = new Creator<PostConsultationModel>() {
		@Override
		public PostConsultationModel createFromParcel(Parcel in) {
			return new PostConsultationModel(in);
		}

		@Override
		public PostConsultationModel[] newArray(int size) {
			return new PostConsultationModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(dateTime);
		dest.writeString(followUpDate);
		dest.writeString(signature);
		dest.writeString(diagnosis);
		dest.writeString(suggestions);
		dest.writeInt(id);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public String getDateTime() { return dateTime; }
	public String getFollowUpDate() { return followUpDate; }
	public String getSignature() { return signature; }
	public String getDiagnosis() {return diagnosis; }
	public String getSuggestions() {return suggestions; }
	public int getId() { return id; }

	// Setters
	public void setFollowUpDate(String followUpDate) {
		this.followUpDate = followUpDate;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}

	public void setId(int id) {
		this.id = id;
	}
}
