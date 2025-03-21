package com.fmv.healthkiosk.feature.tests.data.source.remote.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MedicalPackageResponse{

	@SerializedName("MedicalPackageResponse")
	private List<MedicalPackageResponseItem> medicalPackageResponse;

	public List<MedicalPackageResponseItem> getMedicalPackageResponse(){
		return medicalPackageResponse;
	}
}