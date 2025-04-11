package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class DoctorResponse{

	@SerializedName("gender")
	private String gender;

	@SerializedName("imageBase64")
	private String imageBase64;

	@SerializedName("password")
	private String password;

	@SerializedName("review")
	private double review;

	@SerializedName("dob")
	private String dob;

	@SerializedName("name")
	private String name;

	@SerializedName("specialization")
	private String specialization;

	@SerializedName("id")
	private int id;

	@SerializedName("state")
	private String state;

	@SerializedName("clinic")
	private String clinic;

	@SerializedName("email")
	private String email;

	@SerializedName("age")
	private String age;

	@SerializedName("status")
	private String status;

	public String getGender(){
		return gender;
	}

	public String getImageBase64(){
		return imageBase64;
	}

	public String getPassword(){
		return password;
	}

	public double getReview(){
		return review;
	}

	public String getDob(){
		return dob;
	}

	public String getName(){
		return name;
	}

	public String getSpecialization(){
		return specialization;
	}

	public int getId(){
		return id;
	}

	public String getState(){
		return state;
	}

	public String getClinic(){
		return clinic;
	}

	public String getEmail(){
		return email;
	}

	public String getAge(){
		return age;
	}

	public String getStatus(){
		return status;
	}
}