package com.github.sebastianengel.cityguide;

/**
 * Class providing the list of modules used in the "release" build type.
 * 
 * @author Sebastian Engel
 */
public final class Modules {

    private Modules() {}

    public static Object[] list() {
        return new Object[] {
            new CityGuideModule()
        };
    }
}
