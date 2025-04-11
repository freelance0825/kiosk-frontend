package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class DoctorModel implements Parcelable {

	private String gender;
	private String imageBase64;
	private String password;
	private double review;
	private String dob;
	private String name;
	private String specialization;
	private int id;
	private String state;
	private String clinic;
	private String email;
	private String age;
	private String status;

	public DoctorModel() {
		// Default constructor
	}

	protected DoctorModel(Parcel in) {
		gender = in.readString();
		imageBase64 = in.readString();
		password = in.readString();
		review = in.readDouble();
		dob = in.readString();
		name = in.readString();
		specialization = in.readString();
		id = in.readInt();
		state = in.readString();
		clinic = in.readString();
		email = in.readString();
		age = in.readString();
		status = in.readString();
	}

	public static final Creator<DoctorModel> CREATOR = new Creator<DoctorModel>() {
		@Override
		public DoctorModel createFromParcel(Parcel in) {
			return new DoctorModel(in);
		}

		@Override
		public DoctorModel[] newArray(int size) {
			return new DoctorModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(gender);
		dest.writeString(imageBase64);
		dest.writeString(password);
		dest.writeDouble(review);
		dest.writeString(dob);
		dest.writeString(name);
		dest.writeString(specialization);
		dest.writeInt(id);
		dest.writeString(state);
		dest.writeString(clinic);
		dest.writeString(email);
		dest.writeString(age);
		dest.writeString(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public String getGender() { return gender; }
	public String getImageBase64() { return imageBase64; }
	public String getPassword() { return password; }
	public double getReview() { return review; }
	public String getDob() { return dob; }
	public String getName() { return name; }
	public String getSpecialization() { return specialization; }
	public int getId() { return id; }
	public String getState() { return state; }
	public String getClinic() { return clinic; }
	public String getEmail() { return email; }
	public String getAge() { return age; }
	public String getStatus() { return status; }

	// Setters
	public void setGender(String gender) { this.gender = gender; }
	public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
	public void setPassword(String password) { this.password = password; }
	public void setReview(double review) { this.review = review; }
	public void setDob(String dob) { this.dob = dob; }
	public void setName(String name) { this.name = name; }
	public void setSpecialization(String specialization) { this.specialization = specialization; }
	public void setId(int id) { this.id = id; }
	public void setState(String state) { this.state = state; }
	public void setClinic(String clinic) { this.clinic = clinic; }
	public void setEmail(String email) { this.email = email; }
	public void setAge(String age) { this.age = age; }
	public void setStatus(String status) { this.status = status; }
}
