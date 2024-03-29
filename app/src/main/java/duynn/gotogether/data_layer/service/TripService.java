package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.execute_trip.ListLocationResponse;
import duynn.gotogether.data_layer.model.dto.execute_trip.LocationResponse;
import duynn.gotogether.data_layer.model.dto.request.ClientUpdateLocationRequest;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.AcceptedTripResponse;
import duynn.gotogether.data_layer.model.dto.response.StartTripResponse;
import duynn.gotogether.data_layer.model.dto.response.TripResponse;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Trip;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface TripService {
//    @POST("trip/publish")
//    Call<TripResponse> publish(@Body Trip trip);

    @POST("trip/publish")
    Call<Trip> publish(@Body Trip trip);

//    @POST("trip/search")
//    Call<ListTripResponse> search(@Body SearchTripRequest searchTripRequest);
    @POST("trip/search")
    Call<List<Trip>> search(@Body ClientTrip searchTripRequest);

    @GET("trip/current/{id}")
    Call<TripResponse> getCurrentTrip(@Path("id") Long id);

    @POST("trip/start/{id}")
    Call<StartTripResponse> startTrip(@Path("id") Long id);

    @POST("location/update-driver-location")
    Call<ListLocationResponse> updateDriverLocation(@Body duynn.gotogether.data_layer.model.dto.execute_trip.ClientUpdateLocationRequest request);

    @POST("location/update-passenger-location")
    Call<LocationResponse> updatePassengerLocation(@Body duynn.gotogether.data_layer.model.dto.execute_trip.ClientUpdateLocationRequest request);

    @POST("location/new-update-driver-location")
    Call<ListLocationResponse> newUpdateDriverLocation(@Body ClientUpdateLocationRequest request);

    @POST("location/new-update-passenger-location")
    Call<LocationResponse> newUpdatePassengerLocation(@Body ClientUpdateLocationRequest request);

    @GET("trip/get-accepted-trip/{id}")
    Call<AcceptedTripResponse> getAcceptedTrip(@Path("id") Long clientId);

    @GET("trip/get-waiting-trip-by-passenger-id/{id}")
    Call<ListTripResponse> getWaitingTripByPassengerId(@Path("id") Long clientId);

    Call<Status> requestFinishPassenger(@Body ClientTripDTO clientTrip);

    @POST("trip/finish/{id}")
    Call<Status> finishTrip(@Path("id") Long id);
    @POST("trip/cancel/{id}")
    Call<Status> driverCancelTrip(@Path("id") Long id);

    @POST("clienttrip/cancel/{tripId}/{clientId}")
    Call<Status> passengerCancelTrip(@Path("tripId") Long id, @Path("clientId") Long clientId);
}
