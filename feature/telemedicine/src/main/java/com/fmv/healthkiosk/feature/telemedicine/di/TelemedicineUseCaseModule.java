package com.fmv.healthkiosk.feature.telemedicine.di;

import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.CancelMyAppointmentsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetAllNotificationsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetAvailableDoctorsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetMyAppointmentsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.UpdateMyAppointmentsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.CancelMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAllNotificationsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAvailableDoctorsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.UpdateMyApppointmentsUseCase;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class TelemedicineUseCaseModule {

    @Provides
    GetMyAppointmentsUseCase provideGetMyAppointmentsUseCase(TelemedicineRepository telemedicineRepository) {
        return new GetMyAppointmentsInteractor(telemedicineRepository);
    }

    @Provides
    GetAvailableDoctorsUseCase provideGetAvailableDoctorsUseCase(TelemedicineRepository telemedicineRepository) {
        return new GetAvailableDoctorsInteractor(telemedicineRepository);
    }

    @Provides
    GetAllNotificationsUseCase provideGetAllNotificationsUseCase(TelemedicineRepository telemedicineRepository) {
        return new GetAllNotificationsInteractor(telemedicineRepository);
    }

    @Provides
    public static UpdateMyApppointmentsUseCase provideUpdateMyAppointmentsUseCase(TelemedicineRepository telemedicineRepository) {
        return new UpdateMyAppointmentsInteractor(telemedicineRepository);
    }

    @Provides
    CancelMyAppointmentsUseCase provideCancelMyAppointmentsUseCase(TelemedicineRepository telemedicineRepository) {
        return new CancelMyAppointmentsInteractor(telemedicineRepository);
    }

}
