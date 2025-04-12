package com.fmv.healthkiosk.feature.telemedicine.data.mapper;

import static com.fmv.healthkiosk.feature.telemedicine.data.mapper.DoctorMapper.mapToDoctorModel;

import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.MedicineResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.PatientResponse;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.PostConsultationResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.MedicineModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.PatientModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.PostConsultationModel;

import java.util.ArrayList;
import java.util.List;

public class AppointmentMapper {

    public static AppointmentModel mapToAppointmentModel(AppointmentResponse response) {
        AppointmentModel model = new AppointmentModel();

        model.setDoctor(mapToDoctorModel(response.getDoctor()));
        model.setDateTime(response.getDateTime());
        model.setImageBase64(response.getImageBase64());
        model.setPatientModel(mapToPatientModel(response.getPatient()));
        model.setSpecialization(response.getSpecialization());
        model.setHealthComplaints(response.getHealthComplaints());
        model.setId(response.getId());
        model.setPostConsultationModel(mapToPostConsultationModel(response.getPostConsultation()));

        return model;
    }

    public static PatientModel mapToPatientModel(PatientResponse response) {
        if (response == null) return null;

        PatientModel model = new PatientModel();
        model.setAddress(response.getAddress());
        model.setGender(response.getGender());
        model.setImageBase64(response.getImageBase64());
        model.setDateOfBirth(response.getDateOfBirth());
        model.setName(response.getName());
        model.setPhoneNumber(response.getPhoneNumber());
        model.setId(response.getId());
        model.setAge(response.getAge());
        model.setEmail(response.getEmail());

        return model;
    }


    public static PostConsultationModel mapToPostConsultationModel(PostConsultationResponse response) {
        if (response == null) return null;

        PostConsultationModel model = new PostConsultationModel();
        model.setDateTime(response.getDateTime());
        model.setFollowUpDate(response.getFollowUpDate());
        model.setSignature(response.getSignature());
        model.setDiagnosis(response.getDiagnosis());
        model.setSuggestions(response.getSuggestions());
        model.setId(response.getId());
        model.setMedicines(fromResponseList(response.getMedicineResponses()));

        return model;
    }

    public static MedicineModel fromResponse(MedicineResponse response) {
        if (response == null) return null;

        MedicineModel model = new MedicineModel();
        model.setAfternoon(response.getAfternoon());
        model.setDuration(response.getDuration());
        model.setImageBase64(response.getImageBase64());
        model.setNotes(response.getNotes());
        model.setNight(response.getNight());
        model.setName(response.getName());
        model.setPostConsultationId(response.getPostConsultationId());
        model.setMorning(response.getMorning());

        return model;
    }

    public static List<MedicineModel> fromResponseList(List<MedicineResponse> responses) {
        if (responses == null) return new ArrayList<>();

        List<MedicineModel> models = new ArrayList<>();
        for (MedicineResponse response : responses) {
            models.add(fromResponse(response));
        }
        return models;
    }
}
