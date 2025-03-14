package com.fmv.healthkiosk.feature.auth.data.source.remote.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    @SerializedName("image")
    private String image;

    public RegisterRequest(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
