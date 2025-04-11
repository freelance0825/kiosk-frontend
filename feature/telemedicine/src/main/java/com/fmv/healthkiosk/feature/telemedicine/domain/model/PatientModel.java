package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientModel implements Parcelable {

	private String address;
	private String gender;
	private String imageBase64;
	private String dateOfBirth;
	private String name;
	private String phoneNumber;
	private int id;
	private String age;
	private String email;

	public PatientModel() {}

	protected PatientModel(Parcel in) {
		address = in.readString();
		gender = in.readString();
		imageBase64 = in.readString();
		dateOfBirth = in.readString();
		name = in.readString();
		phoneNumber = in.readString();
		id = in.readInt();
		age = in.readString();
		email = in.readString();
	}

	public static final Creator<PatientModel> CREATOR = new Creator<PatientModel>() {
		@Override
		public PatientModel createFromParcel(Parcel in) {
			return new PatientModel(in);
		}

		@Override
		public PatientModel[] newArray(int size) {
			return new PatientModel[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(address);
		dest.writeString(gender);
		dest.writeString(imageBase64);
		dest.writeString(dateOfBirth);
		dest.writeString(name);
		dest.writeString(phoneNumber);
		dest.writeInt(id);
		dest.writeString(age);
		dest.writeString(email);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	// Getters
	public String getAddress() { return address; }
	public String getGender() { return gender; }
	public String getImageBase64() { return imageBase64; }
	public String getDateOfBirth() { return dateOfBirth; }
	public String getName() { return name; }
	public String getPhoneNumber() { return phoneNumber; }
	public int getId() { return id; }
	public String getAge() { return age; }
	public String getEmail() { return email; }

	// Optional setters (if needed)
	public void setAddress(String address) { this.address = address; }
	public void setGender(String gender) { this.gender = gender; }
	public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
	public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }
	public void setName(String name) { this.name = name; }
	public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
	public void setId(int id) { this.id = id; }
	public void setAge(String age) { this.age = age; }
	public void setEmail(String email) { this.email = email; }
}
