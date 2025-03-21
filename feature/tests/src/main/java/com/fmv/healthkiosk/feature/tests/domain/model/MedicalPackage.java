package com.fmv.healthkiosk.feature.tests.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicalPackage implements Parcelable {

    private String name;
    private String icon;
    private String testPresets;
    private int id;

    public MedicalPackage(String name, String icon, String testPresets, int id) {
        this.name = name;
        this.icon = icon;
        this.testPresets = testPresets;
        this.id = id;
    }

    protected MedicalPackage(Parcel in) {
        name = in.readString();
        icon = in.readString();
        testPresets = in.readString();
        id = in.readInt();
    }

    public static final Creator<MedicalPackage> CREATOR = new Creator<MedicalPackage>() {
        @Override
        public MedicalPackage createFromParcel(Parcel in) {
            return new MedicalPackage(in);
        }

        @Override
        public MedicalPackage[] newArray(int size) {
            return new MedicalPackage[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(icon);
        dest.writeString(testPresets);
        dest.writeInt(id);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getTestPresets() {
        return testPresets;
    }

    public int getId() {
        return id;
    }
}
