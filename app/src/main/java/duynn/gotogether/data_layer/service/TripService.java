package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.SearchTripResponse;
import duynn.gotogether.data_layer.model.model.Trip;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TripService {
    @POST("trip/publish")
    Call<Trip> publish(@Body Trip trip);

    @POST("trip/search")
    Call<SearchTripResponse> search(@Body SearchTripRequest searchTripRequest);
}
