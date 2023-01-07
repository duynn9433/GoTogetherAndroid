package duynn.gotogether.data_layer.repository;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import duynn.gotogether.data_layer.model.dto.execute_trip.ClientLocationDTO;
import duynn.gotogether.data_layer.model.dto.execute_trip.ListLocationResponse;
import duynn.gotogether.data_layer.model.dto.execute_trip.LocationResponse;
import duynn.gotogether.data_layer.model.dto.firebase.UpdateTokenRequest;
import duynn.gotogether.data_layer.model.dto.request.ClientUpdateLocationRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.TransportWithoutOwner;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.ClientService;
import duynn.gotogether.domain_layer.common.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClientRepo {
    public static ClientRepo instance;
    private ClientService clientService;


    public static ClientRepo getInstance(String token) {
        if (instance == null) {
            instance = new ClientRepo(token);
        }
        return instance;
    }

    public ClientRepo(String token) {
        clientService = RetrofitClient.getInstance().createServiceWithToken(ClientService.class, token);
    }

    public void getClientWithId(Long id, MutableLiveData<Client> client, MutableLiveData<Status> status) {
        Call<Client> call = clientService.getClientWithId(id);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    client.setValue(response.body());
                    status.setValue(new Status("success", "get client successfully"));
                } else {
                    status.setValue(new Status("fail", "get client fail"));
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                status.setValue(new Status("fail", "get client fail"));
                t.printStackTrace();
            }
        });
    }

    public void addTransport(Transport transport, MutableLiveData<Status> status, MutableLiveData<Client> client) {
        Call<Status> call = clientService.addTransport(client.getValue().getId(), transport);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    status.setValue(response.body());
                    Client client1 = client.getValue();
                    TransportWithoutOwner transportWithoutOwner = new TransportWithoutOwner(transport);
                    client1.getTransports().add(transportWithoutOwner);
                    client.setValue(client1);
                } else {
                    status.setValue(new Status("fail", "add transport fail"));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                status.setValue(new Status("fail", "add transport fail"));
                t.printStackTrace();
            }
        });
    }

    public void updateFcmToken(String fcmToken, Long clientId) {
        UpdateTokenRequest updateTokenRequest = new UpdateTokenRequest(fcmToken, clientId);
        Call<Status> call = clientService.updateFcmToken(updateTokenRequest);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    System.out.println("update fcm token successfully");
                } else {
                    System.out.println("update fcm token fail");
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                System.out.println("update fcm token fail");
                t.printStackTrace();
            }
        });
    }

    public void updateDriverLocation(Client location,
                                     List<Long> passengerIds,
                                     MutableLiveData<List<Client>> passengerLocation) {
//        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
//                .passengerIDs(passengerIds)
//                .location(location)
//                .build();
        String passengerIdsString = Arrays.toString(passengerIds.toArray());
        Log.d("passengerIdsString", passengerIdsString);
        Call<List<Client>> call = clientService.updateDriverLocation(location, passengerIds);
        System.out.println(call.request().toString());
        call.enqueue(new Callback<List<Client>>() {
            @Override
            public void onResponse(Call<List<Client>> call, Response<List<Client>> response) {
                if (response.isSuccessful()) {
                    List<Client> listLocationResponse = response.body();
                    passengerLocation.postValue(listLocationResponse);
                }
            }

            @Override
            public void onFailure(Call<List<Client>> call, Throwable t) {

            }
        });
    }

    public void updatePassengerLocation(Client location,
                                        Long driverId,
                                        MutableLiveData<Client> driverLocation) {
//        ClientUpdateLocationRequest request = ClientUpdateLocationRequest.builder()
//                .driverId(driverId)
//                .location(location)
//                .build();
        Call<Client> call = clientService.updatePassengerLocation(location, driverId);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    Client locationResponse = response.body();
                    driverLocation.postValue(locationResponse);
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });
    }
}
