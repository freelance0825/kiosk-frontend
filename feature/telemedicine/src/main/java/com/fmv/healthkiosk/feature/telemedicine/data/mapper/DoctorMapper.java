package com.fmv.healthkiosk.feature.telemedicine.data.mapper;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.DoctorResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;

public class DoctorMapper {

	public static DoctorModel mapToDoctorModel(DoctorResponse response) {
		DoctorModel model = new DoctorModel();

		model.setGender(response.getGender());
		model.setImageBase64(response.getImageBase64());
		model.setPassword(response.getPassword());
		model.setReview(response.getReview());
		model.setDob(response.getDob());
		model.setName(response.getName());
		model.setSpecialization(response.getSpecialization());
		model.setId(response.getId());
		model.setState(response.getState());
		model.setClinic(response.getClinic());
		model.setEmail(response.getEmail());
		model.setAge(response.getAge());
		model.setStatus(response.getStatus());

		return model;
	}
}
