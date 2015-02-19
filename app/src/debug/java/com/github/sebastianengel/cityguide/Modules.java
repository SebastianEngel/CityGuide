package com.github.sebastianengel.cityguide;

/**
 * Class providing the list of modules used in the "debug" build type.
 *
 * @author Sebastian Engel
 */
public final class Modules {

    private Modules() {}

    public static Object[] list(CityGuideApp app) {
        return new Object[] {
            new CityGuideModule(app)
        };
    }
}
