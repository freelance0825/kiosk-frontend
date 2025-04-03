package com.fmv.healthkiosk.feature.tests.domain.usecase;

import com.fmv.healthkiosk.feature.tests.domain.model.MedicalPackage;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MedicalPackageUseCase {
    Single<List<MedicalPackage>> getMedicalPackages();
}
