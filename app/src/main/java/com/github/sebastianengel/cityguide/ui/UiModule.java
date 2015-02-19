package com.github.sebastianengel.cityguide.ui;

import dagger.Module;

/**
 * Module providing UI related dependencies.
 *
 * @author Sebastian Engel
 */
@Module(
    injects = {
        MainActivity.class
    },
    complete = false,
    library = false
)
public final class UiModule {
}
