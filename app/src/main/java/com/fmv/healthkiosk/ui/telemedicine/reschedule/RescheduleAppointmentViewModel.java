package com.fmv.healthkiosk.ui.telemedicine.reschedule;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentResponse;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetDoctorTimeslotsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class RescheduleAppointmentViewModel extends BaseViewModel {

    private final UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase;
    private final GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase;

    final int appointmentId;
    final DoctorModel doctorModel;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<AppointmentModel> updatedAppointments = new MutableLiveData<>(null);
    final MutableLiveData<DoctorTimeslotModel> doctorTimeslots = new MutableLiveData<>(null);

    final MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    final MutableLiveData<String> selectedTime = new MutableLiveData<>("");
    final MutableLiveData<Boolean> isAppointmentSubmitted = new MutableLiveData<>(false);

    private final CompositeDisposable disposables = new CompositeDisposable();


    @Inject
    public RescheduleAppointmentViewModel(SavedStateHandle savedStateHandle, UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase, GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase) {
        super(savedStateHandle);
        this.updateMyApppointmentsUseCase = updateMyApppointmentsUseCase;
        this.getDoctorTimeslotsUseCase = getDoctorTimeslotsUseCase;
        this.doctorModel = getArgument("doctor");

        Integer id = savedStateHandle.get("appointmentId");
        if (id == null) {
            throw new IllegalArgumentException("Appointment ID is missing in navigation arguments.");
        }
        this.appointmentId = id;
        Log.d("RescheduleAppointmentViewModel", "Appointment ID in ViewModel: " + appointmentId);
        getDoctorTimeslots(null);
    }

    public void updateMyAppointments() {
        Log.d("RescheduleAppointmentViewModel", "Updating appointment ID: " + appointmentId);
        isLoading.setValue(true);

        String dateTimeTemp = selectedDate.getValue() + " " + selectedTime.getValue();
        String dateTime = convertDate(dateTimeTemp);

        AppointmentRequest appointmentRequest = new AppointmentRequest();
        appointmentRequest.setDateTime(dateTime);

        disposables.add(
                updateMyApppointmentsUseCase.execute(appointmentId, appointmentRequest)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                success -> {
                                    updatedAppointments.setValue(success);
                                    isAppointmentSubmitted.setValue(true);
                                },
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        )
        );
    }

    public void getDoctorTimeslots(String date) {
        int doctorId = doctorModel.getId();

        isLoading.setValue(true);
        disposables.add(
                getDoctorTimeslotsUseCase.execute(doctorId, date)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(doctorTimeslots::setValue, throwable -> {
                            errorMessage.setValue(throwable.getMessage());
                        }));
    }

    private String convertDate(String input) {
        // Parse the original string into a LocalDateTime
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm", Locale.ENGLISH);
        LocalDateTime localDateTime = LocalDateTime.parse(input, inputFormatter);

        // Convert to OffsetDateTime with UTC offset
        OffsetDateTime offsetDateTime = localDateTime.atOffset(ZoneOffset.UTC);

        // Format to ISO 8601 with offset
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        return offsetDateTime.format(outputFormatter);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
