package com.fmv.healthkiosk.feature.telemedicine.data.mapper;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.DoctorTimeslotResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorTimeslotMapper {

    public static DoctorTimeslotModel mapToModel(DoctorTimeslotResponse response) {
        if (response == null) return null;

        DoctorTimeslotModel model = new DoctorTimeslotModel();
        model.setDate(response.getDate());
        model.setAvailableTimeSlots(response.getAvailableTimeSlots());
        return model;
    }

    public static List<DoctorTimeslotModel> mapToModelList(List<DoctorTimeslotResponse> responses) {
        List<DoctorTimeslotModel> models = new ArrayList<>();
        if (responses != null) {
            for (DoctorTimeslotResponse response : responses) {
                models.add(mapToModel(response));
            }
        }
        return models;
    }
}
