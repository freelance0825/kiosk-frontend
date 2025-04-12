package com.fmv.healthkiosk.feature.tests.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TestResult implements Parcelable {
    private String name;
    private float value;
    private String extension;
    private String status;
    private int statusFlag;
    private boolean isGeneralTest;

    public TestResult(String name, float value, String extension, String status, int statusFlag, boolean isGeneralTest) {
        this.name = name;
        this.value = value;
        this.extension = extension;
        this.status = status;
        this.statusFlag = statusFlag;
        this.isGeneralTest = isGeneralTest;
    }

    protected TestResult(Parcel in) {
        name = in.readString();
        value = in.readFloat();
        extension = in.readString();
        status = in.readString();
        statusFlag = in.readInt();
        isGeneralTest = in.readInt() == 1;
    }

    public static final Creator<TestResult> CREATOR = new Creator<TestResult>() {
        @Override
        public TestResult createFromParcel(Parcel in) {
            return new TestResult(in);
        }

        @Override
        public TestResult[] newArray(int size) {
            return new TestResult[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeFloat(value);
        parcel.writeString(extension);
        parcel.writeString(status);
        parcel.writeInt(statusFlag);
        parcel.writeInt(isGeneralTest ? 1 : 0);
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public String getExtension() {
        return extension;
    }

    public String getStatus() {
        return status;
    }

    public float getStatusFlag() {
        return statusFlag;
    }

    public boolean getIsGeneralTest() {
        return isGeneralTest;
    }
}
