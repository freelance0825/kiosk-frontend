package com.fmv.healthkiosk.feature.tests.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestHistoryRequest {

	@SerializedName("tests")
	private List<TestsResultResponse> tests;

	@SerializedName("name")
	private String packageName;

	@SerializedName("patientId")
	private int userId;

	public List<TestsResultResponse> getTests() {
		return tests;
	}

	public void setTests(List<TestsResultResponse> tests) {
		this.tests = tests;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
}