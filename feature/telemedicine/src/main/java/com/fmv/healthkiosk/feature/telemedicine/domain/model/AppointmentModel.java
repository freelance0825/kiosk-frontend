package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AppointmentModel implements Parcelable {

	private DoctorModel doctor;
	private String dateTime;
	private String imageBase64;
	private PatientModel patientModel;
	private String specialization;
	private String healthComplaints;
	private int id;
	private PostConsultationModel postConsultationModel;

	public AppointmentModel() {}

	protected AppointmentModel(Parcel in) {
		doctor = in.readParcelable(DoctorModel.class.getClassLoader());
		dateTime = in.readString();
		imageBase64 = in.readString();
		patientModel = in.readParcelable(PatientModel.class.getClassLoader());
		specialization = in.readString();
		healthComplaints = in.readString();
		id = in.readInt();
		postConsultationModel = in.readParcelable(PostConsultationModel.class.getClassLoader());
	}

	public static final Creator<AppointmentModel> CREATOR = new Creator<AppointmentModel>() {
		@Override
		public AppointmentModel createFromParcel(Parcel in) {
			return new AppointmentModel(in);
		}

		@Override
		public AppointmentModel[] newArray(int size) {
			return new AppointmentModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(doctor, flags);
		dest.writeString(dateTime);
		dest.writeString(imageBase64);
		dest.writeParcelable(patientModel, flags);
		dest.writeString(specialization);
		dest.writeString(healthComplaints);
		dest.writeInt(id);
		dest.writeParcelable(postConsultationModel, flags);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public DoctorModel getDoctor() { return doctor; }
	public String getDateTime() { return dateTime; }
	public String getImageBase64() { return imageBase64; }
	public PatientModel getPatient() { return patientModel; }
	public String getSpecialization() { return specialization; }
	public String getHealthComplaints() { return healthComplaints; }
	public int getId() { return id; }
	public PostConsultationModel getPostConsultation() { return postConsultationModel; }

	// Setters
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}

	public void setDoctor(DoctorModel doctor) {
		this.doctor = doctor;
	}

	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}

	public void setPatientModel(PatientModel patientModel) {
		this.patientModel = patientModel;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public void setHealthComplaints(String healthComplaints) {
		this.healthComplaints = healthComplaints;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPostConsultationModel(PostConsultationModel postConsultationModel) {
		this.postConsultationModel = postConsultationModel;
	}
}
