package com.fmv.healthkiosk.ui.telemedicine.makeappointment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorTimeslotModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetDoctorTimeslotsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.MakeAppointmentsUseCase;

import org.threeten.bp.LocalDateTime;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.ChronoField;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MakeAppointmentViewModel extends BaseViewModel {

    private final MakeAppointmentsUseCase makeAppointmentsUseCase;
    private final GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase;
    private final AccountUseCase accountUseCase;

    final DoctorModel doctor;

    final MutableLiveData<Boolean> isStartingSpeech = new MutableLiveData<>(false);
    final MutableLiveData<Boolean> isLoadingSpeech = new MutableLiveData<>(false);
    final MutableLiveData<String> healthComplaints = new MutableLiveData<>("");

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<DoctorModel>> doctorList = new MutableLiveData<>();
    final MutableLiveData<DoctorTimeslotModel> doctorTimeslots = new MutableLiveData<>(null);

    final MutableLiveData<Integer> userId = new MutableLiveData<>(0);

    final MutableLiveData<String> selectedDate = new MutableLiveData<>("");
    final MutableLiveData<String> selectedTime = new MutableLiveData<>("");


    final MutableLiveData<Boolean> isAppointmentSubmitted = new MutableLiveData<>(false);

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MakeAppointmentViewModel(SavedStateHandle savedStateHandle, MakeAppointmentsUseCase makeAppointmentsUseCase, GetDoctorTimeslotsUseCase getDoctorTimeslotsUseCase, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.makeAppointmentsUseCase = makeAppointmentsUseCase;
        this.getDoctorTimeslotsUseCase = getDoctorTimeslotsUseCase;
        this.accountUseCase = accountUseCase;
        this.doctor = getArgument("doctor");

        observeProfileData();
        getDoctorTimeslots(null);
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.getUserID()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userId::setValue, throwable -> userId.setValue(0)));
    }

    public void makeAppointment() {
        int doctorId = doctor.getId();
        int patientId = accountUseCase.getUserID().blockingFirst();
        String doctorName = doctor.getName();
        String specialization = doctor.getSpecialization();

        String healthComplaints = this.healthComplaints.getValue();
        String dateTimeTemp = selectedDate.getValue() + " " + selectedTime.getValue();
        String dateTime = convertDate(dateTimeTemp);

        isLoading.setValue(true);
        disposables.add(
                makeAppointmentsUseCase.execute(doctorId, patientId, doctorName, healthComplaints, specialization, dateTime, null)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(appointmentResponse -> {
                            isAppointmentSubmitted.setValue(true);
                        }, throwable -> {
                            errorMessage.setValue(throwable.getMessage());
                        }));
    }

    public void getDoctorTimeslots(String date) {
        int doctorId = doctor.getId();

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
