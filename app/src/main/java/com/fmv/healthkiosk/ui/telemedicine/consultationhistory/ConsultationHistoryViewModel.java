package com.fmv.healthkiosk.ui.telemedicine.consultationhistory;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.AppointmentModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ConsultationHistoryViewModel extends BaseViewModel {

    private final GetMyAppointmentsUseCase getMyAppointmentsUseCase;
    private final AccountUseCase accountUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<AppointmentModel>> myAppointmentList = new MutableLiveData<>();

    // PAGINATION CONFIG
    final MutableLiveData<List<AppointmentModel>> pagedDoctorItems = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextDoctorButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackDoctorButton = new MutableLiveData<>(false);

    private final int DOCTOR_PAGE_SIZE = 3;
    private int currentPageIndex = 0;

    private final CompositeDisposable disposables = new CompositeDisposable();

    @Inject
    public ConsultationHistoryViewModel(SavedStateHandle savedStateHandle, GetMyAppointmentsUseCase getMyAppointmentsUseCase, AccountUseCase accountUseCase) {
        super(savedStateHandle);
        this.getMyAppointmentsUseCase = getMyAppointmentsUseCase;
        this.accountUseCase = accountUseCase;

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
                                success -> {
                                    List<AppointmentModel> sortedList = success.stream().sorted((a1, a2) -> a2.getDateTime().compareTo(a1.getDateTime())).collect(Collectors.toList());
                                    myAppointmentList.setValue(sortedList);
                                    loadCurrentPage();
                                },
                                throwable -> {
                                    errorMessage.setValue(throwable.getMessage());
                                }
                        ));
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
