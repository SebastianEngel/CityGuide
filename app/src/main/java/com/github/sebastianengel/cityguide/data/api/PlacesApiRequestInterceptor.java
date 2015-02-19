package com.github.sebastianengel.cityguide.data.api;

import com.github.sebastianengel.cityguide.BuildConfig;

import retrofit.RequestInterceptor;

/**
 * Interceptor adding common request parameters to every request to the Google Places API.
 *
 * @author Sebastian Engel
 */
public class PlacesApiRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestFacade request) {
        request.addEncodedQueryParam("key", BuildConfig.GOOGLE_API_KEY);
    }

}
