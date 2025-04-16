package com.fmv.healthkiosk.ui.telemedicine.consultnow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.model.DoctorModel;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAvailableDoctorsUseCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class ConsultNowViewModel extends BaseViewModel {

    private final GetAvailableDoctorsUseCase getAvailableDoctorsUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<List<DoctorModel>> doctorList = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    // PAGINATION CONFIG
    final MutableLiveData<List<DoctorModel>> pagedDoctorItems = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextDoctorButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackDoctorButton = new MutableLiveData<>(false);

    private final int DOCTOR_PAGE_SIZE = 3;
    private int currentPageIndex = 0;

    @Inject
    public ConsultNowViewModel(SavedStateHandle savedStateHandle, GetAvailableDoctorsUseCase getAvailableDoctorsUseCase) {
        super(savedStateHandle);
        this.getAvailableDoctorsUseCase = getAvailableDoctorsUseCase;

        getAvailableDoctors();
    }

    public void getAvailableDoctors() {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                getAvailableDoctorsUseCase.execute()
                        .subscribeOn(Schedulers.io())
                        .map(doctors -> {
                            doctors.sort((d1, d2) -> Double.compare(d2.getReview(), d1.getReview()));
                            return doctors;
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                success -> {
                                    doctorList.setValue(success);
                                    loadCurrentPage();
                                },
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        )
        );
    }

    private void loadCurrentPage() {
        int fromIndex = currentPageIndex * DOCTOR_PAGE_SIZE;
        int toIndex = Math.min(fromIndex + DOCTOR_PAGE_SIZE, Objects.requireNonNull(doctorList.getValue()).size());

        List<DoctorModel> pageItems = new ArrayList<>(doctorList.getValue().subList(fromIndex, toIndex));
        while (pageItems.size() < DOCTOR_PAGE_SIZE) {
            pageItems.add(null);
        }

        pagedDoctorItems.setValue(pageItems);

        int maxPage = (int) Math.ceil((double) doctorList.getValue().size() / DOCTOR_PAGE_SIZE);
        showBackDoctorButton.setValue(currentPageIndex > 0);
        showNextDoctorButton.setValue(currentPageIndex < maxPage - 1);
    }

    public void nextDoctorPage() {
        int maxPage = (int) Math.ceil((double) doctorList.getValue().size() / DOCTOR_PAGE_SIZE);
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
