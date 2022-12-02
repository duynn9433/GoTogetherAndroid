package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.config.di.ModelMapperConfig;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripResponse;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ListClientTripResponse;
import duynn.gotogether.data_layer.model.dto.request.PassengerFinishRequest;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.dto.response.TripResponse;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.ClientTripService;
import duynn.gotogether.data_layer.service.TripService;
import duynn.gotogether.domain_layer.common.Constants;
import org.modelmapper.ModelMapper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ClientTripRepo {
    private static final String TAG = ClientTripRepo.class.getSimpleName();
    private static ClientTripRepo instance;
    private ClientTripService clientTripService;
    public static ClientTripRepo getInstance(String token) {
        if (instance == null) {
            instance = new ClientTripRepo(token);
        }
        return instance;
    }

    public ClientTripRepo(String token) {
        clientTripService = RetrofitClient.getInstance().createServiceWithToken(ClientTripService.class, token);
    }


    public void getAll(MutableLiveData<String> status,
                       MutableLiveData<String> message,
                       MutableLiveData<List<ClientTrip>> clientTrips,
                       Long id) {
        ModelMapper modelMapper = ModelMapperConfig.getInstance();
        Call<ListClientTripResponse> call = clientTripService.getAll(id);
        call.enqueue(new Callback<ListClientTripResponse>() {
            @Override
            public void onResponse(Call<ListClientTripResponse> call, Response<ListClientTripResponse> response) {
                if (response.isSuccessful()) {
                    ListClientTripResponse listClientTripResponse = response.body();
                    if (listClientTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        //mapper
                        List<ClientTrip> clientTripList = listClientTripResponse.getData()
                                .stream()
                                .map(clientTripDTO -> modelMapper.map(clientTripDTO, ClientTrip.class))
                                .collect(Collectors.toList());
                        //set data
                        clientTrips.setValue(clientTripList);
                        message.postValue(listClientTripResponse.getMessage());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(listClientTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ListClientTripResponse> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });

    }
    public void regitTrip(ClientTripDTO clientTripDTO,
                          MutableLiveData<String> status,
                          MutableLiveData<String> message,
                          MutableLiveData<ClientTripResponse> clientTripResponse) {
        Call<ClientTripResponse> call = clientTripService.regit(clientTripDTO);
        call.enqueue(new Callback<ClientTripResponse>() {
            @Override
            public void onResponse(Call<ClientTripResponse> call, Response<ClientTripResponse> response) {
                if (response.isSuccessful()) {
                    ClientTripResponse clientTripResponse1 = response.body();
                    if (clientTripResponse1.getStatus().equals(Constants.SUCCESS)) {
                        clientTripResponse.setValue(clientTripResponse1);
                        message.postValue(clientTripResponse1.getMessage());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(clientTripResponse1.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    message.postValue("Dữ liệu trả về không hợp lệ");
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ClientTripResponse> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void update(MutableLiveData<String> status,
                       MutableLiveData<String> message,
                       ClientTrip clientTrip) {
        ModelMapper mapper = ModelMapperConfig.getInstance();
        ClientTripDTO clientTripDTO = mapper.map(clientTrip, ClientTripDTO.class);
        Call<ClientTripResponse> call = clientTripService.update(clientTripDTO);
        call.enqueue(new Callback<ClientTripResponse>() {
            @Override
            public void onResponse(Call<ClientTripResponse> call, Response<ClientTripResponse> response) {
                if (response.isSuccessful()) {
                    ClientTripResponse clientTripResponse = response.body();
                    if (clientTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        message.postValue(clientTripResponse.getMessage());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(clientTripResponse.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ClientTripResponse> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void finishTrip(PassengerFinishRequest passengerFinishRequest,
                           MutableLiveData<String> status,
                           MutableLiveData<String> message) {
        Call<Status> call = clientTripService.finishTrip(passengerFinishRequest);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status1 = response.body();
                    if (status1.getStatus().equals(Constants.SUCCESS)) {
                        message.postValue(status1.getMessage());
                        status.postValue(Constants.SUCCESS);
                    } else {
                        message.postValue(status1.getMessage());
                        status.postValue(Constants.FAIL);
                    }
                } else {
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });

    }
}
