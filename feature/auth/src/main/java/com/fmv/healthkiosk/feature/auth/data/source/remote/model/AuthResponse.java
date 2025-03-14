package com.fmv.healthkiosk.feature.auth.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class AuthResponse{

	@SerializedName("imageBase64")
	private String imageBase64;

	@SerializedName("address")
	private String address;

	@SerializedName("phoneNumber")
	private String phoneNumber;

	@SerializedName("gender")
	private String gender;

	@SerializedName("name")
	private String name;

	@SerializedName("dateOfBirth")
	private String dateOfBirth;

	@SerializedName("id")
	private long id;

	@SerializedName("age")
	private String age;

	public String getImageBase64(){
		return imageBase64;
	}

	public String getAddress(){
		return address;
	}

	public String getPhoneNumber(){
		return phoneNumber;
	}

	public String getGender(){
		return gender;
	}

	public String getName(){
		return name;
	}

	public String getDateOfBirth(){
		return dateOfBirth;
	}

	public long getId(){
		return id;
	}

	public String getAge(){
		return age;
	}
}