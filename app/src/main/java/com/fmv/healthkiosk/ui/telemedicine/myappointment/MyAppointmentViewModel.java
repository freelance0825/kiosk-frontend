package com.fmv.healthkiosk.ui.telemedicine.myappointment;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class MyAppointmentViewModel extends BaseViewModel {

    private final GetMyAppointmentsUseCase getMyAppointmentsUseCase;

    private final CancelMyAppointmentsUseCase cancelMyAppointmentsUseCase;

    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<AppointmentModel>> myAppointmentList = new MutableLiveData<>();

    final MutableLiveData<Integer> selectedAppointmentToCancel = new MutableLiveData<>(null);

    // PAGINATION CONFIG
    final MutableLiveData<List<AppointmentModel>> pagedDoctorItems = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextDoctorButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackDoctorButton = new MutableLiveData<>(false);

    private final int DOCTOR_PAGE_SIZE = 3;
    private int currentPageIndex = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public MyAppointmentViewModel(SavedStateHandle savedStateHandle, GetMyAppointmentsUseCase getMyAppointmentsUseCase,
                                  AccountUseCase accountUseCase, CancelMyAppointmentsUseCase cancelMyAppointmentsUseCase) {
        super(savedStateHandle);
        this.getMyAppointmentsUseCase = getMyAppointmentsUseCase;
        this.accountUseCase = accountUseCase;
        this.cancelMyAppointmentsUseCase = cancelMyAppointmentsUseCase;

        // Fetch appointments on initialization
        getMyAppointments();
    }

    public void getMyAppointments() {
        int userId = accountUseCase.getUserID().blockingFirst();

        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getMyAppointmentsUseCase.execute(userId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                list -> {
                                    // Filter only appointments with postConsultation present
                                    List<AppointmentModel> sortedList = list.stream()
                                            .filter(item -> item.getPostConsultation() == null).sorted((a1, a2) -> a2.getDateTime().compareTo(a1.getDateTime())).collect(Collectors.toList());

                                    myAppointmentList.setValue(sortedList);
                                    loadCurrentPage();
                                },
                                throwable -> {
                                    errorMessage.setValue(throwable.getMessage());
                                }
                        ));
    }

    public void cancelAppointment(int appointmentId) {
        disposables.add(
                cancelMyAppointmentsUseCase.execute(appointmentId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(this::getMyAppointments)
                        .subscribe(
                                success -> {
                                    getMyAppointments();
                                },
                                throwable -> {
                                    errorMessage.setValue("Failed to cancel appointment. Please try again later.");
                                }
                        )
        );
    }

    private void loadCurrentPage() {
        int fromIndex = currentPageIndex * DOCTOR_PAGE_SIZE;
        int toIndex = Math.min(fromIndex + DOCTOR_PAGE_SIZE, Objects.requireNonNull(myAppointmentList.getValue()).size());

        List<AppointmentModel> pageItems = new ArrayList<>(myAppointmentList.getValue().subList(fromIndex, toIndex));
        while (pageItems.size() < DOCTOR_PAGE_SIZE) {
            pageItems.add(null);
        }

        pagedDoctorItems.setValue(pageItems);

        int maxPage = (int) Math.ceil((double) myAppointmentList.getValue().size() / DOCTOR_PAGE_SIZE);
        showBackDoctorButton.setValue(currentPageIndex > 0);
        showNextDoctorButton.setValue(currentPageIndex < maxPage - 1);
    }

    public void nextDoctorPage() {
        int maxPage = (int) Math.ceil((double) myAppointmentList.getValue().size() / DOCTOR_PAGE_SIZE);
        if (currentPageIndex < maxPage - 1) {
            currentPageIndex++;
            loadCurrentPage();
        }
    }

    public void previousDoctorPage() {
        if (currentPageIndex > 0) {
            currentPageIndex--;
            loadCurrentPage();
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
