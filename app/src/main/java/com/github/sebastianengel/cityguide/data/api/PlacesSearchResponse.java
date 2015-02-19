package com.github.sebastianengel.cityguide.data.api;

import com.github.sebastianengel.cityguide.data.model.Place;

import java.util.ArrayList;
import java.util.List;

/**
 * Response object for a places search request.
 * <p>
 *     Note: To avoid that extra class we could also set a custom deserializer onto the RestAdapter.
 * </p>
 *
 * @author Sebastian Engel
 */
public class PlacesSearchResponse {

    public enum Status {OK, ZERO_RESULTS, OVER_QUERY_LIMIT, REQUEST_DENIED, INVALID_REQUEST}

    public Status status;
    public String errorMessage;
    public List<Place> places = new ArrayList<>();

}
