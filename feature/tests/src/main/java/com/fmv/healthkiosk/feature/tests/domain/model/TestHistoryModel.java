package com.fmv.healthkiosk.feature.tests.domain.model;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class TestHistoryModel implements Parcelable {

	private String userAddress;
	private String createdAt;
	private List<TestsResultModel> tests;
	private String userDob;
	private String userPhoneNumber;
	private String userGender;
	private String packageName;
	private String userName;
	private int userId;
	private String userAge;

	public TestHistoryModel() {
		// Default constructor
	}

	protected TestHistoryModel(Parcel in) {
		userAddress = in.readString();
		createdAt = in.readString();
		tests = in.createTypedArrayList(TestsResultModel.CREATOR);
		userDob = in.readString();
		userPhoneNumber = in.readString();
		userGender = in.readString();
		packageName = in.readString();
		userName = in.readString();
		userId = in.readInt();
		userAge = in.readString();
	}

	public static final Creator<TestHistoryModel> CREATOR = new Creator<TestHistoryModel>() {
		@Override
		public TestHistoryModel createFromParcel(Parcel in) {
			return new TestHistoryModel(in);
		}

		@Override
		public TestHistoryModel[] newArray(int size) {
			return new TestHistoryModel[size];
		}
	};

	public String getUserAddress() {
		return userAddress;
	}

	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public List<TestsResultModel> getTests() {
		return tests;
	}

	public void setTests(List<TestsResultModel> tests) {
		this.tests = tests;
	}

	public String getUserDob() {
		return userDob;
	}

	public void setUserDob(String userDob) {
		this.userDob = userDob;
	}

	public String getUserPhoneNumber() {
		return userPhoneNumber;
	}

	public void setUserPhoneNumber(String userPhoneNumber) {
		this.userPhoneNumber = userPhoneNumber;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(userAddress);
		dest.writeString(createdAt);
		dest.writeTypedList(tests);
		dest.writeString(userDob);
		dest.writeString(userPhoneNumber);
		dest.writeString(userGender);
		dest.writeString(packageName);
		dest.writeString(userName);
		dest.writeInt(userId);
		dest.writeString(userAge);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public void setOriginalPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setPackageName(String packageName) {
		if (packageName == null) {
			this.packageName = "CUSTOM_PACKAGE";
			return;
		}
		switch (packageName) {
			case "Basic Package":
				this.packageName = "BASIC_PACKAGE";
				break;
			case "Kid's Package":
				this.packageName = "KIDS_PACKAGE";
				break;
			case "Elderly Package":
				this.packageName = "ELDERLY_PACKAGE";
				break;
			case "Women's Package":
				this.packageName = "WOMENS_PACKAGE";
				break;
			case "Men's Health Package":
				this.packageName = "MEN_HEALTH_PACKAGE";
				break;
			case "Diabetes Package":
				this.packageName = "DIABETES_PACKAGE";
				break;
			case "Heart Package":
				this.packageName = "HEART_PACKAGE";
				break;
			default:
				this.packageName = "CUSTOM_PACKAGE";
				break;
		}
	}

	public String getPackageName() {
		if (this.packageName == null) return "Custom Package";
		switch (this.packageName) {
			case "BASIC_PACKAGE":
				return "Basic Package";
			case "KIDS_PACKAGE":
				return "Kid's Package";
			case "ELDERLY_PACKAGE":
				return "Elderly Package";
			case "WOMENS_PACKAGE":
				return "Women's Package";
			case "MEN_HEALTH_PACKAGE":
				return "Men's Health Package";
			case "DIABETES_PACKAGE":
				return "Diabetes Package";
			case "HEART_PACKAGE":
				return "Heart Package";
			default:
				return "Custom Package";
		}
	}

	public String getOriginalPackageName() {
		return this.packageName;
	}
}
