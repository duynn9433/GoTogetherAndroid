package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.execute_trip.ListLocationResponse;
import duynn.gotogether.data_layer.model.dto.execute_trip.LocationResponse;
import duynn.gotogether.data_layer.model.dto.firebase.UpdateTokenRequest;
import duynn.gotogether.data_layer.model.dto.request.ClientUpdateLocationRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface ClientService {
    @GET("client/{id}")
    Call<Client> getClientWithId(@Path("id") Long id);

    @POST("client/{id}/transport")
    Call<Status> addTransport(@Path("id") Long id, @Body Transport transport);

    @POST("updatetoken")
    Call<Status> updateFcmToken(@Body UpdateTokenRequest updateTokenRequest);

//    @POST("client/update-driver-location")
//    Call<ListLocationResponse> updateDriverLocation(@Body ClientUpdateLocationRequest request);

//    @POST("client/update-driver-location")
//    Call<List<Client>> updateDriverLocation(@Body Client client, @Query("passengerIds") List<Long> passengerIds);
    @POST("client/update-driver-location")
    Call<List<Client>> updateDriverLocation(@Body Client client, @Query("passengerIds") List<Long> passengerIds);

//    @POST("client/update-passenger-location")
//    Call<LocationResponse> updatePassengerLocation(@Body ClientUpdateLocationRequest request);
    @POST("client/update-passenger-location")
    Call<Client> updatePassengerLocation(@Body Client client, @Query("driverId") Long driverId);
}
