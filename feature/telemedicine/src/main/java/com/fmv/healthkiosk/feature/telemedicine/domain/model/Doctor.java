package com.fmv.healthkiosk.feature.telemedicine.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Doctor implements Parcelable {

    private int id;
    private String imageBase64;
    private String name;
    private String specialization;
    private String status;
    private float review;
    private String email;
    private String password;
    private String state;
    private String clinic;
    private String gender;
    private String age;
    private String dob;

    // New fields
    private long dateTime;
   // private boolean isLive;

    public Doctor() {
        // Default constructor
    }

    protected Doctor(Parcel in) {
        id = in.readInt();
        imageBase64 = in.readString();
        name = in.readString();
        specialization = in.readString();
        status = in.readString();
        review = in.readFloat();
        email = in.readString();
        password = in.readString();
        state = in.readString();
        clinic = in.readString();
        gender = in.readString();
        age = in.readString();
        dob = in.readString();
        dateTime = in.readLong();
       // isLive = in.readByte() != 0;
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(imageBase64);
        dest.writeString(name);
        dest.writeString(specialization);
        dest.writeString(status);
        dest.writeFloat(review);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(state);
        dest.writeString(clinic);
        dest.writeString(gender);
        dest.writeString(age);
        dest.writeString(dob);
        dest.writeLong(dateTime);
       // dest.writeByte((byte) (isLive ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getReview() {
        return review;
    }

    public void setReview(float review) {
        this.review = review;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getClinic() {
        return clinic;
    }

    public void setClinic(String clinic) {
        this.clinic = clinic;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }

/*    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }*/
}
