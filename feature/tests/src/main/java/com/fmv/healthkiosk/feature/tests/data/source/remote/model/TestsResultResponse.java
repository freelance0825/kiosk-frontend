package com.fmv.healthkiosk.feature.tests.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class TestsResultResponse {

    @SerializedName("result")
    private String result;

    @SerializedName("name")
    private String name;

    @SerializedName("range")
    private String range;

    @SerializedName("isGeneralTest")
    private Boolean isGeneralTest;

    public Boolean getIsGeneralTest() {
        return isGeneralTest;
    }

    public void setIsGeneralTest(Boolean isGeneralTest) {
        this.isGeneralTest = isGeneralTest;
    }

    @SerializedName("id")
    private Integer id = null;

    public void setResult(String result) {
        this.result = result;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getResult() {
        return result;
    }

    public String getName() {
        return name;
    }

    public String getRange() {
        return range;
    }

    public int getId() {
        return id;
    }
}