package com.fmv.healthkiosk.ui.home.landing;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.ui.home.model.MenuItem;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

@HiltViewModel
public class HomeLandingViewModel extends BaseViewModel {

    private final AccountUseCase accountUseCase;
    private final CompositeDisposable disposables = new CompositeDisposable();

    List<MenuItem> menuList = Arrays.asList(
            new MenuItem("general_checkup", "General Checkup", R.drawable.asset_icon_checkup),
            new MenuItem("advanced_test", "Advanced Test", R.drawable.asset_icon_advanced_test),
            new MenuItem("report", "Report", R.drawable.asset_icon_health_report),
            new MenuItem("e_health_record", "E-Health Record", R.drawable.asset_icon_health_record),
            new MenuItem("telemedicine", "Telemedicine", R.drawable.asset_icon_telemedicine)
    );


    final MutableLiveData<String> username = new MutableLiveData<>();

    @Inject
    public HomeLandingViewModel(SavedStateHandle savedStateHandle, AccountUseCase accountUseCase) {
        super(savedStateHandle);

        this.accountUseCase = accountUseCase;
        observeProfileData();
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    username.setValue(name.replaceAll("\\s.*", ""));
                }, throwable -> username.setValue("Unknown")));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }
}
