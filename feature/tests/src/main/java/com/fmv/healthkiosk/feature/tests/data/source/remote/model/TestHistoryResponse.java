package com.fmv.healthkiosk.feature.tests.data.source.remote.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class TestHistoryResponse{

	@SerializedName("userAddress")
	private String userAddress;

	@SerializedName("createdAt")
	private String createdAt;

	@SerializedName("tests")
	private List<TestsResultResponse> tests;

	@SerializedName("userDob")
	private String userDob;

	@SerializedName("userPhoneNumber")
	private String userPhoneNumber;

	@SerializedName("userGender")
	private String userGender;

	@SerializedName("packageName")
	private String packageName;

	@SerializedName("userName")
	private String userName;

	@SerializedName("userId")
	private int userId;

	@SerializedName("userAge")
	private String userAge;

	public String getUserAddress(){
		return userAddress;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public List<TestsResultResponse> getTests(){
		return tests;
	}

	public String getUserDob(){
		return userDob;
	}

	public String getUserPhoneNumber(){
		return userPhoneNumber;
	}

	public String getUserGender(){
		return userGender;
	}

	public String getPackageName(){
		return packageName;
	}

	public String getUserName(){
		return userName;
	}

	public int getUserId(){
		return userId;
	}

	public String getUserAge(){
		return userAge;
	}
}