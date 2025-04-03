package com.fmv.healthkiosk.feature.tests.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class MedicalPackageResponseItem{

	@SerializedName("tests")
	private String tests;

	@SerializedName("name")
	private String name;

	@SerializedName("icon")
	private String icon;

	@SerializedName("id")
	private int id;

	public String getTests(){
		return tests;
	}

	public String getName(){
		return name;
	}

	public String getIcon(){
		return icon;
	}

	public int getId(){
		return id;
	}
}