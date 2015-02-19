package com.github.sebastianengel.cityguide.ui;

import dagger.Module;

/**
 * Module providing UI related dependencies.
 *
 * @author Sebastian Engel
 */
@Module(
    injects = {
        PlacesListFragment.class
    },
    complete = false,
    library = false
)
public final class UiModule {
}
