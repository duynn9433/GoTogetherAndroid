package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.execute_trip.ClientUpdateLocationRequest;
import duynn.gotogether.data_layer.model.dto.execute_trip.ListLocationResponse;
import duynn.gotogether.data_layer.model.dto.execute_trip.LocationResponse;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.model.dto.response.StartTripResponse;
import duynn.gotogether.data_layer.model.dto.response.TripResponse;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.TripService;
import duynn.gotogether.domain_layer.common.Constants;
import org.modelmapper.ModelMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRepo {
    private static final String TAG = TripRepo.class.getSimpleName();
    private static TripRepo instance;
    private TripService tripService;

    public static TripRepo getInstance(String token) {
        if (instance == null) {
            instance = new TripRepo(token);
        }
        return instance;
    }

    public TripRepo(String token) {
        tripService = RetrofitClient.getInstance().createServiceWithToken(TripService.class, token);
    }

    public void publish(Trip trip,
                        MutableLiveData<Trip> tripMutableLiveData,
                        MutableLiveData<String> status,
                        MutableLiveData<String> message,
                        Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        Client client = sessionManager.getClient();
        trip.setDriver(client);
        Call<TripResponse> call = tripService.publish(trip);

        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (response.isSuccessful()) {
                    TripResponse publicTripResponse = response.body();
                    if (publicTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        tripMutableLiveData.setValue(publicTripResponse.getTrip());
                        message.postValue(publicTripResponse.getMessage());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(publicTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
//                    Log.d(TAG, "publish: " + response.body().toString());
                } else {
//                    Log.d(TAG, "publish req: " + response.body().toString());
//                    PublicTripResponse publicTripResponse = response.body();
//                    message.postValue(publicTripResponse.getMessage());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage() +"\n" + call.request().toString());
//                Log.d(TAG, "onFailure gson: " + RetrofitClient.getInstance().getGson().toJson(trip));
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);

            }
        });
    }

    public Trip publishSync(Trip trip) throws IOException {
        Call<TripResponse> call = RetrofitClient.getInstance().getTripService().publish(trip);
        Response<TripResponse> response = call.execute();
        if (response.isSuccessful()) {
            return response.body().getTrip();
        } else {
            throw new IOException(response.message());
        }
    }

    public void searchTrip(Context context,
                           MutableLiveData<SearchTripRequest> searchTripRequest,
                           MutableLiveData<ListTripResponse> searchTripResponse,
                           MutableLiveData<String> status){
        SearchTripRequest request = searchTripRequest.getValue();
        Call<ListTripResponse> call = tripService.search(request);
        Log.d(TAG, "searchTrip: " + call.request().toString());

        call.enqueue(new Callback<ListTripResponse>() {
            @Override
            public void onResponse(Call<ListTripResponse> call, Response<ListTripResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    searchTripResponse.postValue(response.body());
                    status.postValue(Constants.SUCCESS);
                } else {
                    Log.d(TAG, "onResponse: " + response.message());
                    searchTripResponse.postValue(response.body());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ListTripResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void getCurrentTrip(MutableLiveData<String> status,
                               MutableLiveData<String> message,
                               MutableLiveData<Trip> currentTrip,
                               Long id) {
        Call<TripResponse> call = tripService.getCurrentTrip(id);
        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (response.isSuccessful()) {
                    TripResponse tripResponse = response.body();
                    if (tripResponse.getStatus().equals(Constants.SUCCESS)) {
                        currentTrip.postValue(tripResponse.getTrip());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        currentTrip.postValue(null);
                        message.postValue(tripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void startTrip(MutableLiveData<String> status,
                          MutableLiveData<String> message,
                          MutableLiveData<Trip> currentTrip,
                          MutableLiveData<List<ClientTrip>> clientTrips) {
        Trip trip = currentTrip.getValue();
        Call<StartTripResponse> call = tripService.startTrip(trip.getId());
        call.enqueue(new Callback<StartTripResponse>() {
            @Override
            public void onResponse(Call<StartTripResponse> call, Response<StartTripResponse> response) {
                if (response.isSuccessful()) {
                    StartTripResponse startTripResponse = response.body();
                    if (startTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        currentTrip.postValue(startTripResponse.getTrip());
                        clientTrips.postValue(startTripResponse.getClientTrips());
                        message.postValue(Constants.START_TRIP_SUCCESS);
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(startTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<StartTripResponse> call, Throwable t) {
                status.postValue(Constants.FAIL);
            }
        });

    }

    public void updateDriverLocation(Location location,
                                     Long tripId, Long clientId,
                                     MutableLiveData<List<Location>> passengerLocation) {
        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
                .tripId(tripId)
                .clientId(clientId)
                .location(location)
                .build();
        Call<ListLocationResponse> call = tripService.updateDriverLocation(request);
        call.enqueue(new Callback<ListLocationResponse>() {
            @Override
            public void onResponse(Call<ListLocationResponse> call, Response<ListLocationResponse> response) {
                if (response.isSuccessful()) {
                    ListLocationResponse listLocationResponse = response.body();
                    if (listLocationResponse.getStatus().equals(Constants.SUCCESS)) {
                        passengerLocation.postValue(listLocationResponse.getLocationList());
                    }
                }
            }

            @Override
            public void onFailure(Call<ListLocationResponse> call, Throwable t) {

            }
        });
    }

    public void updatePassengerLocation(Location location,
                                        Long tripId, Long clientId,
                                        MutableLiveData<Location> driverLocation) {
        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
                .tripId(tripId)
                .clientId(clientId)
                .location(location)
                .build();
        Call<LocationResponse> call = tripService.updatePassengerLocation(request);
        call.enqueue(new Callback<LocationResponse>() {
            @Override
            public void onResponse(Call<LocationResponse> call, Response<LocationResponse> response) {
                if (response.isSuccessful()) {
                    LocationResponse locationResponse = response.body();
                    if (locationResponse.getStatus().equals(Constants.SUCCESS)) {
                        driverLocation.postValue(locationResponse.getLocation());
                    }
                }
            }

            @Override
            public void onFailure(Call<LocationResponse> call, Throwable t) {

            }
        });
    }

    public void getAcceptedTrip(MutableLiveData<String> status,
                                MutableLiveData<String> message,
                                MutableLiveData<Trip> acceptedTrip,
                                Long id) {
        Call<TripResponse> call = tripService.getAcceptedTrip(id);
        call.enqueue(new Callback<TripResponse>() {
            @Override
            public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                if (response.isSuccessful()) {
                    TripResponse tripResponse = response.body();
                    if (tripResponse.getStatus().equals(Constants.SUCCESS)) {
                        acceptedTrip.postValue(tripResponse.getTrip());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        acceptedTrip.postValue(null);
                        message.postValue(tripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<TripResponse> call, Throwable t) {
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void getWaitingTrips(MutableLiveData<String> status,
                                MutableLiveData<String> message,
                                MutableLiveData<List<Trip>> waitingTrips, Long id) {
        Call<ListTripResponse> call = tripService.getWaitingTripByPassengerId(id);
        call.enqueue(new Callback<ListTripResponse>() {
            @Override
            public void onResponse(Call<ListTripResponse> call, Response<ListTripResponse> response) {
                if (response.isSuccessful()) {
                    ListTripResponse listTripResponse = response.body();
                    if (listTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        waitingTrips.postValue(listTripResponse.getTrips());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        waitingTrips.postValue(new ArrayList<>());
                        message.postValue(listTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ListTripResponse> call, Throwable t) {
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void cancelTrip(MutableLiveData<String> status,
                           MutableLiveData<String> message,
                           Trip trip, Long clientId) {
        //TODO: implement cancel trip
    }

    public void requestFinishPassenger(ClientTrip clientTrip,
                                       MutableLiveData<String> status,
                                       MutableLiveData<String> message) {

    }

    public void finishTrip(Long id, MutableLiveData<String> status, MutableLiveData<String> message) {
        Call<Status> call = tripService.finishTrip(id);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status finishTripResponse = response.body();
                    if (finishTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(finishTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                status.postValue(Constants.FAIL);
            }
        });
    }
}
