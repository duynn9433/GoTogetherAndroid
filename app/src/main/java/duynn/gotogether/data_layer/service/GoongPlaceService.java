package duynn.gotogether.data_layer.service;

import com.google.android.libraries.places.api.model.Place;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.GoongPlaceAutocompleteResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoongPlaceService {
    @GET("Place/AutoComplete")
    Call<GoongPlaceAutocompleteResult> placeAutocompleteWithText(
            @Query("api_key") String apiKey,
            @Query("input") String input);

    @GET("Place/AutoComplete")
    Call<GoongPlaceAutocompleteResult> placeAutocompleteWithLocation(
            @Query("api_key") String apiKey,
            @Query("location") String location);

    @GET("Place/Detail")
    Call<GoongPlaceDetailResult> placeDetailWithPlaceId(
            @Query("place_id") String placeId,
            @Query("api_key") String apiKey);
}
