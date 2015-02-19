package com.github.sebastianengel.cityguide.data.api;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

/**
 * Definition of requests to the Google Places API.
 *
 * @author Sebastian Engel
 */
public interface IPlacesApi {

    @GET("/nearbysearch/json?rankby=distance")
    Observable<PlacesSearchResponse> fetchNearbyPlaces(@Query("location") String location, @Query("types") String types);

}
