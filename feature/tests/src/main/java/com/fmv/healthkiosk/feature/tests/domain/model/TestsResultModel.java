package com.fmv.healthkiosk.feature.tests.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TestsResultModel implements Parcelable {

	private String result;
	private String name;
	private String range;
	private int id;

	private boolean isGeneralTest;

	public TestsResultModel() {}

	protected TestsResultModel(Parcel in) {
		result = in.readString();
		name = in.readString();
		range = in.readString();
		id = in.readInt();
		isGeneralTest = in.readByte() != 0;
	}

	public static final Creator<TestsResultModel> CREATOR = new Creator<TestsResultModel>() {
		@Override
		public TestsResultModel createFromParcel(Parcel in) {
			return new TestsResultModel(in);
		}

		@Override
		public TestsResultModel[] newArray(int size) {
			return new TestsResultModel[size];
		}
	};

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(result);
		parcel.writeString(name);
		parcel.writeString(range);
		parcel.writeInt(id);
		parcel.writeByte((byte) (isGeneralTest ? 1 : 0));
	}

	public boolean getIsGeneralTest() {
		return isGeneralTest;
	}

	public void setIsGeneralTest(boolean isGeneralTest) {
		this.isGeneralTest = isGeneralTest;
	}

	public void setOriginalRange(String range) {
		this.range = range;
	}

	public void setRange(String range) {
		if (range == null) {
			this.range = null;
			return;
		}
		switch (range) {
            case "Normal":
				this.range = "NORMAL";
				break;
			case "High":
				this.range = "HIGH";
				break;
			default:
				this.range = "LOW";
				break;
		}
	}

	public String getRange() {
		if (this.range == null) return "Low";
		switch (this.range) {
            case "NORMAL":
				return "Normal";
			case "HIGH":
				return "High";
			default:
				return "Low";
		}
	}

	public String getOriginalRange() {
		return this.range;
	}
}
