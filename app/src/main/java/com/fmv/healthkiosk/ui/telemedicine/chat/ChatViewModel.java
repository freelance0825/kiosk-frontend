package com.fmv.healthkiosk.ui.telemedicine.chat;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetDoctorTimeslotsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.SendMessageUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.utils.ChatbotCommands;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ChatViewModel extends BaseViewModel {

    private final SendMessageUseCase sendMessageUseCase;
    private final UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase;
    private final GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase;

    final DoctorModel doctorModel;
    final int appointmentId;

    final MutableLiveData<Boolean> isStartingSpeech = new MutableLiveData<>(false);
    final MutableLiveData<Boolean> isLoadingSpeech = new MutableLiveData<>(false);
    final MutableLiveData<String> speechMessage = new MutableLiveData<>("");

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    final MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    final MutableLiveData<String> selectedTime = new MutableLiveData<>("");

    final MutableLiveData<ArrayList<ChatMessage>> chatMessageList = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<Map<String, AppointmentModel>> updatedAppointment = new MutableLiveData<>(null);
    final MutableLiveData<DoctorTimeslotModel> doctorTimeslots = new MutableLiveData<>(null);

    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ChatViewModel(SavedStateHandle savedStateHandle, SendMessageUseCase sendMessageUseCase, UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase, GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase) {
        super(savedStateHandle);
        this.sendMessageUseCase = sendMessageUseCase;
        this.updateMyApppointmentsUseCase = updateMyApppointmentsUseCase;
        this.getDoctorTimeslotsUseCase = getDoctorTimeslotsUseCase;

        this.doctorModel = getArgument("doctor");
        this.appointmentId = getArgument("appointmentId");

        sendMessage(null);
    }

    public void sendMessage(String message) {
        disposables.add(
                sendMessageUseCase.execute(message)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                chatMessageList::setValue,
                                throwable -> Log.e("ChatViewModel", "Error handling message", throwable)
                        )
        );
    }

    public void updateMyAppointments(String message) {
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
                                    Map<String, AppointmentModel> map = new HashMap<>();
                                    map.put(message, success);
                                    updatedAppointment.setValue(map);
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
                        .subscribe(success -> {
                            doctorTimeslots.setValue(success);

                            // Trigger Time Suggestions after successfully fetched Timeslots
                            sendMessage(ChatbotCommands.RESCHEDULE_TIME);
                        }, throwable -> {
                            errorMessage.setValue(throwable.getMessage());
                        }));
    }

    private String convertDate(String input) {
        // Parse the original string into a LocalDateTime
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm", Locale.ENGLISH);
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
