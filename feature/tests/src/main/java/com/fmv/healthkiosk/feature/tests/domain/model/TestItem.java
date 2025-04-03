package com.fmv.healthkiosk.feature.tests.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TestItem implements Parcelable {
    private String id;
    private String name;
    private String testResult = "";
    private int icon;
    private int isTested;
    private boolean isActive;
    private String testExtension = "";
    private boolean isGeneral;

    public TestItem(String id, String name, String testResult, int icon, int isTested, boolean isActive, String testExtension, boolean isGeneral) {
        this.id = id;
        this.name = name;
        this.testResult = testResult;
        this.icon = icon;
        this.isTested = isTested;
        this.isActive = isActive;
        this.testExtension = testExtension;
        this.isGeneral = isGeneral;
    }

    protected TestItem(Parcel in) {
        id = in.readString();
        name = in.readString();
        testResult = in.readString();
        icon = in.readInt();
        isTested = in.readInt();
        isActive = in.readByte() != 0;
        testExtension = in.readString();
        isGeneral = in.readByte() != 0;
    }

    public static final Creator<TestItem> CREATOR = new Creator<TestItem>() {
        @Override
        public TestItem createFromParcel(Parcel in) {
            return new TestItem(in);
        }

        @Override
        public TestItem[] newArray(int size) {
            return new TestItem[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(testResult);
        dest.writeInt(icon);
        dest.writeInt(isTested);
        dest.writeByte((byte) (isActive ? 1 : 0));
        dest.writeString(testExtension);
        dest.writeByte((byte) (isGeneral ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int isTested() {
        return isTested;
    }

    public void setTested(int isTested) {
        this.isTested = isTested;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getTestResult() {
        return this.testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public String getTestExtension() {
        return this.testExtension;
    }

    public void setTestExtension(String testExtension) {
        this.testExtension = testExtension;
    }

    public boolean isGeneral() {
        return isGeneral;
    }
}
