package com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class NotificationResponse{

	@SerializedName("isRescheduled")
	private boolean isRescheduled;

	@SerializedName("isCancelled")
	private boolean isCancelled;

	@SerializedName("apptDoctorSpecialization")
	private String apptDoctorSpecialization;

	@SerializedName("appointmentId")
	private int appointmentId;

	@SerializedName("appDoctorId")
	private int appDoctorId;

	@SerializedName("apptDateTime")
	private String apptDateTime;

	@SerializedName("isBooked")
	private boolean isBooked;

	@SerializedName("id")
	private int id;

	@SerializedName("appUserId")
	private int appUserId;

	@SerializedName("apptDoctorName")
	private String apptDoctorName;

	@SerializedName("apptUserName")
	private String apptUserName;

	@SerializedName("createdAt")
	private String createAt;

	public boolean isIsRescheduled(){
		return isRescheduled;
	}

	public boolean isIsCancelled(){
		return isCancelled;
	}

	public String getApptDoctorSpecialization(){
		return apptDoctorSpecialization;
	}

	public int getAppointmentId(){
		return appointmentId;
	}

	public int getAppDoctorId(){
		return appDoctorId;
	}

	public String getApptDateTime(){
		return apptDateTime;
	}

	public boolean isIsBooked(){
		return isBooked;
	}

	public int getId(){
		return id;
	}

	public int getAppUserId(){
		return appUserId;
	}

	public String getApptDoctorName(){
		return apptDoctorName;
	}

	public String getApptUserName(){
		return apptUserName;
	}

	public String getCreateAt(){
		return createAt;
	}
}