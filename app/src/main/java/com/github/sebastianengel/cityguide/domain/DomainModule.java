package com.github.sebastianengel.cityguide.domain;

import com.github.sebastianengel.cityguide.data.api.IPlacesApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;

/**
 * Module providing service classes that are part of the domain layer.
 *
 * @author Sebastian Engel
 */
@Module(
    complete = false,
    library = true
)
public final class DomainModule {

    @Provides @Singleton
    PlacesService providePlacesService(ReactiveLocationProvider locationProvider, IPlacesApi placesApi) {
        return new PlacesService(locationProvider, placesApi);
    }

}
