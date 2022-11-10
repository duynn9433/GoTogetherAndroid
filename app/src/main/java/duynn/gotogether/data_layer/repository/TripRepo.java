package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;

import java.io.IOException;

import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.SearchTripResponse;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.TripService;
import duynn.gotogether.domain_layer.common.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripRepo {
    private static final String TAG = TripRepo.class.getSimpleName();
    private static TripRepo instance;

    public static TripRepo getInstance() {
        if (instance == null) {
            instance = new TripRepo();
        }
        return instance;
    }

    public void publish(Trip trip,
                        MutableLiveData<Trip> tripMutableLiveData,
                        MutableLiveData<String> status,
                        Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getToken();
        Client client = sessionManager.getClient();
        trip.setDriver(client);
        Call<Trip> call = RetrofitClient.getInstance().createServiceWithToken(TripService.class,token).publish(trip);

        call.enqueue(new Callback<Trip>() {
            @Override
            public void onResponse(Call<Trip> call, Response<Trip> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "publish: " + response.body().toString());
                    status.postValue(Constants.SUCCESS);
                } else {
                    Log.d(TAG, "publish req: ");
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<Trip> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage() +"\n" + call.request().toString());
//                Log.d(TAG, "onFailure gson: " + RetrofitClient.getInstance().getGson().toJson(trip));
                status.postValue(Constants.FAIL);

            }
        });
    }

    public Trip publishSync(Trip trip) throws IOException {
        Call<Trip> call = RetrofitClient.getInstance().getTripService().publish(trip);
        Response<Trip> response = call.execute();
        if (response.isSuccessful()) {
            return response.body();
        } else {
            throw new IOException(response.message());
        }
    }

    public void searchTrip(Context context,
                           MutableLiveData<SearchTripRequest> searchTripRequest,
                           MutableLiveData<SearchTripResponse> searchTripResponse,
                           MutableLiveData<String> status){
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getToken();
        SearchTripRequest request = searchTripRequest.getValue();
        Call<SearchTripResponse> call = RetrofitClient.getInstance().createServiceWithToken(
                TripService.class, token).search(request);
        Log.d(TAG, "searchTrip: " + call.request().toString());

        call.enqueue(new Callback<SearchTripResponse>() {
            @Override
            public void onResponse(Call<SearchTripResponse> call, Response<SearchTripResponse> response) {
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
            public void onFailure(Call<SearchTripResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                status.postValue(Constants.FAIL);
            }
        });
    }
}
