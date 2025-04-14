package com.fmv.healthkiosk.feature.telemedicine.data.mapper;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.NotificationResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.NotificationModel;

public class NotificationMapper {
	public static NotificationModel fromResponse(NotificationResponse response) {
		NotificationModel model = new NotificationModel();
		model.setRescheduled(response.isIsRescheduled());
		model.setCancelled(response.isIsCancelled());
		model.setApptDoctorSpecialization(response.getApptDoctorSpecialization());
		model.setAppointmentId(response.getAppointmentId());
		model.setAppDoctorId(response.getAppDoctorId());
		model.setApptDateTime(response.getApptDateTime());
		model.setBooked(response.isIsBooked());
		model.setId(response.getId());
		model.setAppUserId(response.getAppUserId());
		model.setApptDoctorName(response.getApptDoctorName());
		model.setApptUserName(response.getApptUserName());
		model.setCreateAt(response.getCreateAt());
		return model;
	}
}
