package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripResponse;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ListClientTripResponse;
import duynn.gotogether.data_layer.model.dto.request.PassengerFinishRequest;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.dto.response.TripResponse;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Trip;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientTripService {
    @GET("clienttrip/getall/{id}")
    Call<ListClientTripResponse> getAll(@Path("id") Long id);

    @POST("clienttrip/regit")
    Call<ClientTrip> regit(@Body ClientTrip clientTrip);

//    @POST("clienttrip/update")
//    Call<ClientTripResponse> update(@Body ClientTripDTO clientTripDTO);
    @POST("clienttrip/update")
    Call<ClientTrip> update(@Body ClientTrip clientTrip);
    @POST("clienttrip/finish")
    Call<Status> finishTrip(@Body Comment comment);

    @POST("clienttrip/cancel/{tripId}/{clientId}")
    Call<Status> passengerCancelTrip(@Path("tripId") Long id, @Path("clientId") Long clientId);

    @GET("clienttrip/get-client-trip-uncommented/{clientId}")
    Call<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse> getClientTripUncommented(@Path("clientId") Long clientId);
}
