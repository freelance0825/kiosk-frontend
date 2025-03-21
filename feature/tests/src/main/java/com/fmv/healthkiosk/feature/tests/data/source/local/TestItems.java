package com.fmv.healthkiosk.feature.tests.data.source.local;

import com.fmv.healthkiosk.feature.tests.R;
import com.fmv.healthkiosk.feature.tests.domain.model.TestItem;

import java.util.ArrayList;
import java.util.List;

public class TestItems {
    public static final List<TestItem> testItemList = new ArrayList<>();

    static {
        testItemList.add(new TestItem("height", "Height", "", R.drawable.ic_height, 0, false));
        testItemList.add(new TestItem("weight", "Weight", "", R.drawable.ic_weight, 0, false));
        testItemList.add(new TestItem("temperature", "Temperature", "", R.drawable.ic_temperature, 0, false));
        testItemList.add(new TestItem("oximeter", "Oximeter", "", R.drawable.ic_oximeter, 0, false));
        testItemList.add(new TestItem("body_fat", "Body Fat", "", R.drawable.ic_body_fat, 0, false));
        testItemList.add(new TestItem("blood_pressure", "Blood Pressure", "", R.drawable.ic_blood_pressure, 0, false));
    }
}
