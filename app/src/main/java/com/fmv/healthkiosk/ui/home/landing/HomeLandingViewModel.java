package com.fmv.healthkiosk.ui.home.landing;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.fmv.healthkiosk.R;
import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import com.fmv.healthkiosk.feature.auth.domain.usecase.AccountUseCase;
import com.fmv.healthkiosk.ui.home.model.MenuItem;

import java.util.ArrayList;
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
//            new MenuItem("e_health_record", "E-Health Record", R.drawable.asset_icon_health_record),
            new MenuItem("telemedicine", "Telemedicine", R.drawable.asset_icon_telemedicine)
    );

    // PAGINATION CONFIG
    final MutableLiveData<List<MenuItem>> pagedMenuItems = new MutableLiveData<>();

    public final MutableLiveData<Boolean> showNextMenuButton = new MutableLiveData<>(false);
    public final MutableLiveData<Boolean> showBackMenuButton = new MutableLiveData<>(false);

    private final int MENU_PAGE_SIZE = 3;
    private int currentPageIndex = 0;


    final MutableLiveData<String> username = new MutableLiveData<>();

    @Inject
    public HomeLandingViewModel(SavedStateHandle savedStateHandle, AccountUseCase accountUseCase) {
        super(savedStateHandle);

        this.accountUseCase = accountUseCase;
        observeProfileData();
        loadCurrentPage();
    }

    private void observeProfileData() {
        disposables.add(accountUseCase.getUsername()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(name -> {
                    username.setValue(name.replaceAll("\\s.*", ""));
                }, throwable -> username.setValue("Unknown")));
    }

    private void loadCurrentPage() {
        int fromIndex = currentPageIndex * MENU_PAGE_SIZE;
        int toIndex = Math.min(fromIndex + MENU_PAGE_SIZE, menuList.size());

        List<MenuItem> pageItems = new ArrayList<>(menuList.subList(fromIndex, toIndex));
        while (pageItems.size() < MENU_PAGE_SIZE) {
            pageItems.add(null);
        }

        pagedMenuItems.setValue(pageItems);

        int maxPage = (int) Math.ceil((double) menuList.size() / MENU_PAGE_SIZE);
        showBackMenuButton.setValue(currentPageIndex > 0);
        showNextMenuButton.setValue(currentPageIndex < maxPage - 1);
    }

    public void nextPage() {
        int maxPage = (int) Math.ceil((double) menuList.size() / MENU_PAGE_SIZE);
        if (currentPageIndex < maxPage - 1) {
            currentPageIndex++;
            loadCurrentPage();
        }
    }

    public void previousPage() {
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
