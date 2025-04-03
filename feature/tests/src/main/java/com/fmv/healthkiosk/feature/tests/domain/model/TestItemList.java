package com.fmv.healthkiosk.feature.tests.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class TestItemList implements Parcelable {
    private List<TestItem> testItemList;

    public TestItemList(List<TestItem> testItemList) {
        this.testItemList = testItemList;
    }

    protected TestItemList(Parcel in) {
        testItemList = new ArrayList<>();
        in.readTypedList(testItemList, TestItem.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(testItemList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TestItemList> CREATOR = new Creator<TestItemList>() {
        @Override
        public TestItemList createFromParcel(Parcel in) {
            return new TestItemList(in);
        }

        @Override
        public TestItemList[] newArray(int size) {
            return new TestItemList[size];
        }
    };

    public List<TestItem> getTestItemList() {
        return testItemList;
    }
}
