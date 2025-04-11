package com.fmv.healthkiosk.feature.telemedicine.utils;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.Appointment;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.Doctor;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.ZonedDateTime;

import java.lang.reflect.Type;

public class AppointmentDeserializer implements JsonDeserializer<Appointment> {

    @Override
    public Appointment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();

        Appointment appointment = new Appointment();
        appointment.setId(obj.get("id").getAsInt());

        // Safely handle null values
        boolean isCancelled = obj.has("isCancelled") && !obj.get("isCancelled").isJsonNull() ? obj.get("isCancelled").getAsBoolean() : false;
        boolean isRescheduled = obj.has("isRescheduled") && !obj.get("isRescheduled").isJsonNull() ? obj.get("isRescheduled").getAsBoolean() : false;

        appointment.setCancelled(isCancelled);
        appointment.setRescheduled(isRescheduled);

        // Set isBooked based on cancellation and rescheduling
        boolean isBooked = !isCancelled && !isRescheduled;
        appointment.setBooked(isBooked);  // Set the isBooked field

        // Handle date parsing with error handling
        try {
            String isoDateTime = obj.has("dateTime") && !obj.get("dateTime").isJsonNull() ? obj.get("dateTime").getAsString() : "";
            if (!isoDateTime.isEmpty()) {
                LocalDateTime ldt = LocalDateTime.parse(isoDateTime);
                ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
                long epochMillis = zdt.toInstant().toEpochMilli();
                appointment.setDateTime(epochMillis);
            } else {
                appointment.setDateTime(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
            appointment.setDateTime(0);
        }

        // Deserialize the doctor object
        Doctor doctor = context.deserialize(obj.get("doctor"), Doctor.class);
        appointment.setDoctor(doctor);

        return appointment;
    }
}
