package com.fmv.healthkiosk.ui.telemedicine.chat;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.data.source.remote.model.AppointmentRequest;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.SendMessageUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;

import java.util.ArrayList;
import java.util.HashMap;
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

    final DoctorModel doctorModel;
    final int appointmentId;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<ArrayList<ChatMessage>> chatMessageList = new MutableLiveData<>(new ArrayList<>());
    final MutableLiveData<Map<String, AppointmentModel>> updatedAppointment = new MutableLiveData<>(null);
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ChatViewModel(SavedStateHandle savedStateHandle, SendMessageUseCase sendMessageUseCase, UpdateMyApppointmentsUseCase updateMyApppointmentsUseCase) {
        super(savedStateHandle);
        this.sendMessageUseCase = sendMessageUseCase;
        this.updateMyApppointmentsUseCase = updateMyApppointmentsUseCase;

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

    public void updateMyAppointments(String message, String dateTime) {
        isLoading.setValue(true);

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

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
