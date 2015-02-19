package com.github.sebastianengel.cityguide.data;

import android.app.Application;

import com.github.sebastianengel.cityguide.BuildConfig;
import com.github.sebastianengel.cityguide.data.api.IPlacesApi;
import com.github.sebastianengel.cityguide.data.api.PlacesApiRequestInterceptor;
import com.github.sebastianengel.cityguide.data.api.PlacesSearchResponse;
import com.github.sebastianengel.cityguide.data.api.PlacesSearchResponseDeserializer;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Module providing data access related dependencies.
 *
 * @author Sebastian Engel
 */
@Module(
    complete = false,
    library = true
)
public class DataModule {

    @Provides @Singleton
    ReactiveLocationProvider provideLocationProvider(Application app) {
        return new ReactiveLocationProvider(app);
    }

    @Provides @Singleton
    IPlacesApi providePlacesApi(RestAdapter restAdapter) {
        return restAdapter.create(IPlacesApi.class);
    }

    @Provides @Singleton
    RestAdapter provideRestAdapter(GsonConverter gsonConverter) {
        // Retrofit used OkHttp by default when on classpath.
        // No need to set a custom instance here since no customizations are needed.
        return new RestAdapter.Builder()
            .setEndpoint(Endpoints.newFixedEndpoint(BuildConfig.GOOGLE_PLACES_API_URL))
            .setRequestInterceptor(new PlacesApiRequestInterceptor())
            .setConverter(gsonConverter)
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
            .build();
    }

    @Provides @Singleton
    GsonConverter provideGsonConverter() {
        return new GsonConverter(new GsonBuilder().registerTypeAdapter(
            PlacesSearchResponse.class, new PlacesSearchResponseDeserializer()).create());
    }

}
