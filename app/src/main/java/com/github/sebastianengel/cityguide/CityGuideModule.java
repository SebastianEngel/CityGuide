package com.github.sebastianengel.cityguide;

import android.app.Application;

import com.github.sebastianengel.cityguide.data.DataModule;
import com.github.sebastianengel.cityguide.domain.DomainModule;
import com.github.sebastianengel.cityguide.ui.UiModule;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * The app's main module.
 *
 * @author Sebastian Engel
 */
@Module(
    includes = {
        UiModule.class,
        DomainModule.class,
        DataModule.class
    },
    injects = {
        CityGuideApp.class
    }
)
public final class CityGuideModule {

    private final CityGuideApp app;

    public CityGuideModule(CityGuideApp app) {
        this.app = app;
    }

    @Provides @Singleton
    Application provideApplication() {
        return app;
    }

}
