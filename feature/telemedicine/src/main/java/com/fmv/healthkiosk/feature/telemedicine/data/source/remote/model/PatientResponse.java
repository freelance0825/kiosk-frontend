package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class PatientResponse {

	@SerializedName("address")
	private String address;

	@SerializedName("gender")
	private String gender;

	@SerializedName("image_base64")
	private String imageBase64;

	@SerializedName("date_of_birth")
	private String dateOfBirth;

	@SerializedName("name")
	private String name;

	@SerializedName("phone_number")
	private String phoneNumber;

	@SerializedName("id")
	private int id;

	@SerializedName("age")
	private String age;

	@SerializedName("email")
	private String email;

	public String getAddress(){
		return address;
	}

	public String getGender(){
		return gender;
	}

	public String getImageBase64(){
		return imageBase64;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public String getName(){
		return name;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public int getId(){
		return id;
	}

	public String getAge(){
		return age;
	}

	public String getEmail(){
		return email;
	}
}