package com.fmv.healthkiosk.ui.auth.register;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.LoginUseCase;
import com.fmv.healthkiosk.feature.auth.domain.usecase.RegisterUseCase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterViewModel extends BaseViewModel {

    private final RegisterUseCase registerUseCase;

    final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    final MutableLiveData<String> registerSuccessMessage = new MutableLiveData<>();
    final MutableLiveData<String> userAge = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public RegisterViewModel(SavedStateHandle savedStateHandle, RegisterUseCase registerUseCase) {
        super(savedStateHandle);

        this.registerUseCase = registerUseCase;
    }

    public void register(String name, String gender, String dob, String phoneNumber) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        disposables.add(
                registerUseCase.execute(name, gender, userAge.getValue(), dob, phoneNumber)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doFinally(() -> isLoading.setValue(false))
                        .subscribe(
                                registerSuccessMessage::setValue,
                                throwable -> errorMessage.setValue(throwable.getMessage())
                        ));
    }

    public void updateAge(String dob) {
        if (dob == null || dob.isEmpty()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date birthDate = sdf.parse(dob);
            Calendar dobCalendar = Calendar.getInstance();
            dobCalendar.setTime(birthDate);

            Calendar today = Calendar.getInstance();
            int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            userAge.setValue(String.valueOf(age));
        } catch (ParseException e) {
            Log.e("RegisterViewModel", "updateAge: " + e.getMessage() );
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
