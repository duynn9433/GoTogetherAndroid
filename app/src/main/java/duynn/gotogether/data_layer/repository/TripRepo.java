package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import duynn.gotogether.data_layer.helper.error_helper.ApiError;
import duynn.gotogether.data_layer.helper.error_helper.ErrorUtils;
import duynn.gotogether.data_layer.model.dto.execute_trip.ClientLocationDTO;
import duynn.gotogether.data_layer.model.dto.execute_trip.ListLocationResponse;
import duynn.gotogether.data_layer.model.dto.execute_trip.LocationResponse;
import duynn.gotogether.data_layer.model.dto.request.ClientUpdateLocationRequest;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.AcceptedTripResponse;
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
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
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
//        Call<TripResponse> call = tripService.publish(trip);
        Call<Trip> call = tripService.publish(trip);

        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.isSuccessful()) {
                    Trip publicTripResponse = response.body();
                    tripMutableLiveData.setValue(publicTripResponse);
                    message.postValue("Chia sẻ chuyến đi thành công");
                    status.postValue(Constants.SUCCESS);
//                    Log.d(TAG, "publish: " + response.body().toString());
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.d(TAG, "error resp: " + errorBody);
                        ApiError apiError = null;
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                        message.postValue(apiError.getMessage());
//                        ToastUseCase.showLongToast(context, apiError.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                    Log.d(TAG, "publish req: " + response.body().toString());
//                    PublicTripResponse publicTripResponse = response.body();
//                    message.postValue(publicTripResponse.getMessage());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);

            }
        });
    }

//    public Trip publishSync(Trip trip) throws IOException {
//        Call<TripResponse> call = RetrofitClient.getInstance().getTripService().publish(trip);
//        Response<TripResponse> response = call.execute();
//        if (response.isSuccessful()) {
//            return response.body().getTrip();
//        } else {
//            throw new IOException(response.message());
//        }
//    }

    public void searchTrip(Context context,
                           MutableLiveData<ClientTrip> searchTripRequest,
                           MutableLiveData<List<Trip>> searchTripResponse,
                           MutableLiveData<String> status) {
        ClientTrip clientTrip = searchTripRequest.getValue();
        Call<List<Trip>> call = tripService.search(clientTrip);
//        Log.d(TAG, "searchTrip: " + call.request().toString());

        call.enqueue(new Callback<List<Trip>>() {
            @Override
            public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    searchTripResponse.postValue(response.body());
                    status.postValue(Constants.SUCCESS);
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.d(TAG, "error resp: " + errorBody);
                        ApiError apiError = null;
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                        ToastUseCase.showLongToast(context, apiError.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    searchTripResponse.postValue(response.body());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<List<Trip>> call, Throwable t) {
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

    public void updateDriverLocation(ClientLocationDTO location,
                                     List<Long> passsengerIds,
                                     MutableLiveData<List<ClientLocationDTO>> passengerLocation) {
        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
                .passengerIDs(passsengerIds)
                .location(location)
                .build();
        Call<ListLocationResponse> call = tripService.newUpdateDriverLocation(request);
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

    public void updatePassengerLocation(ClientLocationDTO location,
                                        Long driverId,
                                        MutableLiveData<ClientLocationDTO> driverLocation) {
        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
                .driverId(driverId)
                .location(location)
                .build();
        Call<LocationResponse> call = tripService.newUpdatePassengerLocation(request);
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
                                MutableLiveData<ClientTrip> currentClientTrip,
                                Long id) {
        Call<AcceptedTripResponse> call = tripService.getAcceptedTrip(id);
        call.enqueue(new Callback<AcceptedTripResponse>() {
            @Override
            public void onResponse(Call<AcceptedTripResponse> call, Response<AcceptedTripResponse> response) {
                if (response.isSuccessful()) {
                    AcceptedTripResponse acceptedTripResponse = response.body();
                    if (acceptedTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        acceptedTrip.postValue(acceptedTripResponse.getTrip());
                        currentClientTrip.postValue(acceptedTripResponse.getClientTrip());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        acceptedTrip.postValue(null);
                        message.postValue(acceptedTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                    acceptedTrip.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<AcceptedTripResponse> call, Throwable t) {
                acceptedTrip.postValue(null);
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

    public void passengerCancelTrip(MutableLiveData<String> status,
                                    MutableLiveData<String> message,
                                    Trip trip, Long clientId) {
        //TODO: implement cancel trip
        Call<Status> call = tripService.passengerCancelTrip(trip.getId(), clientId);
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
                        message.postValue(Constants.FINISH_TRIP_SUCCESS);
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

    public void driverCancelTrip(MutableLiveData<String> status, MutableLiveData<String> message, Trip trip) {
        Call<Status> call = tripService.driverCancelTrip(trip.getId());
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status finishTripResponse = response.body();
                    if (finishTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        status.postValue(Constants.SUCCESS);
                        message.postValue(Constants.FINISH_TRIP_SUCCESS);
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
