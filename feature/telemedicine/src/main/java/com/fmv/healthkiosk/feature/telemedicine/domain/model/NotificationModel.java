package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationModel implements Parcelable {
	private boolean isRescheduled;
	private boolean isCancelled;
	private String apptDoctorSpecialization;
	private int appointmentId;
	private int appDoctorId;
	private String apptDateTime;
	private boolean isBooked;
	private int id;
	private int appUserId;
	private String apptDoctorName;
	private String apptUserName;
	private String createAt;

	// Constructor
	public NotificationModel() {}

	// Getters
	public boolean isRescheduled() {
		return isRescheduled;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public String getApptDoctorSpecialization() {
		return apptDoctorSpecialization;
	}

	public int getAppointmentId() {
		return appointmentId;
	}

	public int getAppDoctorId() {
		return appDoctorId;
	}

	public String getApptDateTime() {
		return apptDateTime;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public int getId() {
		return id;
	}

	public int getAppUserId() {
		return appUserId;
	}

	public String getApptDoctorName() {
		return apptDoctorName;
	}

	public String getApptUserName() {
		return apptUserName;
	}

	public String getCreateAt() {
		return createAt;
	}

	// Setters
	public void setRescheduled(boolean rescheduled) {
		isRescheduled = rescheduled;
	}

	public void setCancelled(boolean cancelled) {
		isCancelled = cancelled;
	}

	public void setApptDoctorSpecialization(String apptDoctorSpecialization) {
		this.apptDoctorSpecialization = apptDoctorSpecialization;
	}

	public void setAppointmentId(int appointmentId) {
		this.appointmentId = appointmentId;
	}

	public void setAppDoctorId(int appDoctorId) {
		this.appDoctorId = appDoctorId;
	}

	public void setApptDateTime(String apptDateTime) {
		this.apptDateTime = apptDateTime;
	}

	public void setBooked(boolean booked) {
		isBooked = booked;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAppUserId(int appUserId) {
		this.appUserId = appUserId;
	}

	public void setApptDoctorName(String apptDoctorName) {
		this.apptDoctorName = apptDoctorName;
	}

	public void setApptUserName(String apptUserName) {
		this.apptUserName = apptUserName;
	}

	public void setCreateAt(String createAt) {
		this.createAt = createAt;
	}

	// Parcelable implementation
	protected NotificationModel(Parcel in) {
		isRescheduled = in.readByte() != 0;
		isCancelled = in.readByte() != 0;
		apptDoctorSpecialization = in.readString();
		appointmentId = in.readInt();
		appDoctorId = in.readInt();
		apptDateTime = in.readString();
		isBooked = in.readByte() != 0;
		id = in.readInt();
		appUserId = in.readInt();
		apptDoctorName = in.readString();
		apptUserName = in.readString();
		createAt = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (isRescheduled ? 1 : 0));
		dest.writeByte((byte) (isCancelled ? 1 : 0));
		dest.writeString(apptDoctorSpecialization);
		dest.writeInt(appointmentId);
		dest.writeInt(appDoctorId);
		dest.writeString(apptDateTime);
		dest.writeByte((byte) (isBooked ? 1 : 0));
		dest.writeInt(id);
		dest.writeInt(appUserId);
		dest.writeString(apptDoctorName);
		dest.writeString(apptUserName);
		dest.writeString(createAt);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
		@Override
		public NotificationModel createFromParcel(Parcel in) {
			return new NotificationModel(in);
		}

		@Override
		public NotificationModel[] newArray(int size) {
			return new NotificationModel[size];
		}
	};
}
