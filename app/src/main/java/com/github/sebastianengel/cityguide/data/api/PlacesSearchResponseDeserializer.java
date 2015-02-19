package com.github.sebastianengel.cityguide.data.api;

import com.github.sebastianengel.cityguide.data.model.Place;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Custom deserializer class used to avoid multiple to rebuild the response JSON structure with
 * custom classes, as it would be required when just using the default JSON converter and annotating fields.
 *
 * @author Sebastian Engel
 */
public class PlacesSearchResponseDeserializer implements JsonDeserializer<PlacesSearchResponse> {

    @Override
    public PlacesSearchResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

        PlacesSearchResponse response = new PlacesSearchResponse();

        JsonObject rootObject = json.getAsJsonObject();

        // Parse the status.
        String status = rootObject.getAsJsonPrimitive("status").getAsString();
        response.status = PlacesSearchResponse.Status.valueOf(status.toUpperCase());

        // If the status is not OK, parse the error message that might be included.
        if (response.status != PlacesSearchResponse.Status.OK) {
            response.errorMessage = rootObject.getAsJsonPrimitive("error_message").getAsString();
        }

        // TODO Google's terms require the app to display attributions if included in the response.

        // Parse the places.
        JsonArray placesArray = rootObject.getAsJsonArray("results");
        JsonObject placeObject;
        JsonElement placeAttributeElement;
        JsonObject geometryObject;
        JsonElement locationElement;
        JsonObject locationObject;
        JsonElement latitudeElement;
        JsonElement longitudeElement;
        Place place;
        for (JsonElement placeElement : placesArray) {
            placeObject = placeElement.getAsJsonObject();
            place = new Place();

            placeAttributeElement = placeObject.get("place_id");
            if (placeAttributeElement != null) {
                place.placeId = placeAttributeElement.getAsString();
            }

            placeAttributeElement = placeObject.get("name");
            if (placeAttributeElement != null) {
                place.name = placeAttributeElement.getAsString();
            }

            placeAttributeElement = placeObject.get("geometry");
            if (placeAttributeElement != null) {
                geometryObject = placeAttributeElement.getAsJsonObject();
                locationElement = geometryObject.get("location");
                if (locationElement != null) {
                    locationObject = locationElement.getAsJsonObject();
                    latitudeElement = locationObject.get("lat");
                    longitudeElement = locationObject.get("lng");

                    if (latitudeElement != null && longitudeElement  != null) {
                        place.latitude = latitudeElement.getAsFloat();
                        place.longitude = longitudeElement.getAsFloat();
                    }
                }
            }

            placeAttributeElement = placeObject.get("rating");
            if (placeAttributeElement != null) {
                place.rating = placeAttributeElement.getAsFloat();
            }

            response.places.add(place);
        }

        return response;
    }

}
