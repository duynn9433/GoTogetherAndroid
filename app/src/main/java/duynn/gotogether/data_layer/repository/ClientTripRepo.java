package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.config.di.ModelMapperConfig;
import duynn.gotogether.data_layer.helper.error_helper.ApiError;
import duynn.gotogether.data_layer.helper.error_helper.ErrorUtils;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripResponse;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ListClientTripResponse;
import duynn.gotogether.data_layer.model.dto.request.PassengerFinishRequest;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.dto.response.TripResponse;
import duynn.gotogether.data_layer.model.model.*;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.ClientTripService;
import duynn.gotogether.data_layer.service.TripService;
import duynn.gotogether.domain_layer.ToastUseCase;
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

    public void regitTrip(ClientTrip clientTrip,
                          MutableLiveData<String> status,
                          MutableLiveData<String> message,
                          MutableLiveData<ClientTrip> clientTripResponse) {
        Call<ClientTrip> call = clientTripService.regit(clientTrip);
        call.enqueue(new Callback<ClientTrip>() {
            @Override
            public void onResponse(Call<ClientTrip> call, Response<ClientTrip> response) {
                if (response.isSuccessful()) {
                    ClientTrip clientTrip = response.body();
                    clientTripResponse.setValue(clientTrip);
                    message.postValue("Đăng kí thành công");
                    status.postValue(Constants.SUCCESS);
                } else {
                    ApiError apiError = null;
                    try {
                        String errorBody = response.errorBody().string();
                        Log.d(TAG, "error resp: " + errorBody);
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                    } catch (IOException e) {
                        apiError = new ApiError();
                        throw new RuntimeException(e);
                    }
                    message.postValue(apiError.getMessage());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ClientTrip> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void update(MutableLiveData<String> status,
                       MutableLiveData<String> message,
                       ClientTrip clientTrip) {
//        ModelMapper mapper = ModelMapperConfig.getInstance();
//        ClientTripDTO clientTripDTO = mapper.map(clientTrip, ClientTripDTO.class);
        Call<ClientTrip> call = clientTripService.update(clientTrip);
        call.enqueue(new Callback<ClientTrip>() {
            @Override
            public void onResponse(Call<ClientTrip> call, Response<ClientTrip> response) {
                if (response.isSuccessful()) {
                    ClientTrip clientTripResponse = response.body();
                    message.postValue("Thành công");
                    status.postValue(Constants.SUCCESS);
                } else {
                    ApiError apiError = null;
                    try {
                        String errorBody = response.errorBody().string();
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                    } catch (IOException e) {
                        apiError = new ApiError();
                        throw new RuntimeException(e);
                    }
                    message.postValue(apiError.getMessage());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<ClientTrip> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }

    public void finishTrip(Comment comment,
                           MutableLiveData<String> status,
                           MutableLiveData<String> message) {
        Call<Status> call = clientTripService.finishTrip(comment);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    Status status1 = response.body();
                    message.postValue(status1.getMessage());
                    status.postValue(Constants.SUCCESS);
                } else {
                    ApiError apiError = null;
                    try {
                        String errorBody = response.errorBody().string();
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                    } catch (IOException e) {
                        apiError = new ApiError();
                        throw new RuntimeException(e);
                    }
                    message.postValue(apiError.getMessage());
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

    public void getUnratedClientTrip(Long id,
                                     MutableLiveData<String> status,
                                     MutableLiveData<String> message,
                                     MutableLiveData<List<ClientTrip>> clientTripList) {
        ModelMapper modelMapper = ModelMapperConfig.getInstance();
        Call<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse> call
                = clientTripService.getClientTripUncommented(id);
        call.enqueue(new Callback<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse>() {
            @Override
            public void onResponse(Call<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse> call,
                                   Response<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse> response) {
                if (response.isSuccessful()) {
                    duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse listClientTripResponse = response.body();
                    if (listClientTripResponse.getStatus().equals(Constants.SUCCESS)) {
                        List<ClientTrip> clientTrips1 = listClientTripResponse.getData()
                                .stream()
                                .map(clientTripDTO -> modelMapper.map(clientTripDTO, ClientTrip.class))
                                .collect(Collectors.toList());
                        clientTripList.setValue(clientTrips1);
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
            public void onFailure(Call<duynn.gotogether.data_layer.model.dto.response.ListClientTripResponse> call, Throwable t) {
                message.postValue(t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }
}
