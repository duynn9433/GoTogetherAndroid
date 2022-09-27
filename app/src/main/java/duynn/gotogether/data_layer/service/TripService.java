package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.model.Trip;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface TripService {
    @POST("trip/publish")
    Call<Boolean> publish(@Body Trip trip);
}
