package com.fmv.healthkiosk.feature.telemedicine.di;

import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetAllNotificationsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetAvailableDoctorsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.interactor.GetMyAppointmentsInteractor;
import com.fmv.healthkiosk.feature.telemedicine.domain.repo.TelemedicineRepository;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAllNotificationsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetAvailableDoctorsUseCase;
import com.fmv.healthkiosk.feature.telemedicine.domain.usecase.GetMyAppointmentsUseCase;

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
}
