package com.fmv.healthkiosk.ui.telemedicine.landing;

import androidx.lifecycle.SavedStateHandle;
import com.fmv.healthkiosk.core.base.ui.BaseViewModel;
import javax.inject.Inject;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class TelemedicineLandingViewModel extends BaseViewModel {

    @Inject
    public TelemedicineLandingViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
