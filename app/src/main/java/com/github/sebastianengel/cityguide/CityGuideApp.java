package com.github.sebastianengel.cityguide;

import android.app.Application;
import android.content.Context;

import dagger.ObjectGraph;
import timber.log.Timber;

/**
 * Application subclass used for application-wide initialization.
 *
 * @author Sebastian Engel
 */
public class CityGuideApp extends Application {

    private ObjectGraph objectGraph;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        buildObjectGraphAndInject();
    }

    private void buildObjectGraphAndInject() {
        objectGraph = ObjectGraph.create(Modules.list());
        objectGraph.inject(this);
    }

    public ObjectGraph getObjectGraph() {
        return objectGraph;
    }

    public void inject(Object object) {
        objectGraph.inject(object);
    }

    public static CityGuideApp get(Context context) {
        return (CityGuideApp) context.getApplicationContext();
    }

}
