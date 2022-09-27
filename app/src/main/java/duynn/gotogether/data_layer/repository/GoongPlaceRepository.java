package duynn.gotogether.data_layer.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import duynn.gotogether.BuildConfig;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.GoongPlaceAutocompleteResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.Prediction;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.service.GoongPlaceService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoongPlaceRepository {
    private GoongPlaceService goongPlaceService;

    private GoongPlaceAutocompleteResult goongPlaceAutocompleteResult;
    private GoongPlaceDetailResult goongPlaceDetailResult;

    public GoongPlaceRepository(GoongPlaceService goongPlaceService) {
        this.goongPlaceService = goongPlaceService;
    }

    public void getPlaceAutoCompleteWithText(String input, MutableLiveData<List<Prediction>> liveData){
        Call<GoongPlaceAutocompleteResult> call =
                goongPlaceService.placeAutocompleteWithText(BuildConfig.GOONG_API_KEY, input);
        call.enqueue(new Callback<GoongPlaceAutocompleteResult>() {
            @Override
            public void onResponse(Call<GoongPlaceAutocompleteResult> call, Response<GoongPlaceAutocompleteResult> response) {
                if (response.isSuccessful()){
                    GoongPlaceAutocompleteResult result = response.body();
//                    Log.d("GoongPlaceRepository", "onResponse: " + result.toString());
                    if (result != null){
                        liveData.postValue(result.getPredictions());
                    }
                }else{
                    liveData.postValue(new ArrayList<>());
//                    Toast.makeText(null, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GoongPlaceAutocompleteResult> call, Throwable t) {
                Log.d("GoongPlaceRepository", "onFailure: " + t.getMessage());
//                Toast.makeText(null, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getPlaceDetail(String placeId, MutableLiveData<GoongPlaceDetailResult> liveData) {
        Call<GoongPlaceDetailResult> call =
                goongPlaceService.placeDetailWithPlaceId(placeId, BuildConfig.GOONG_API_KEY);
        call.enqueue(new Callback<GoongPlaceDetailResult>() {
            @Override
            public void onResponse(Call<GoongPlaceDetailResult> call, Response<GoongPlaceDetailResult> response) {
                if (response.isSuccessful()){
                    GoongPlaceDetailResult result = response.body();
                    if (result != null){
                        liveData.postValue(result);
                    }
                }else{
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GoongPlaceDetailResult> call, Throwable t) {
                Log.d("GoongPlaceRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getPlaceDetailWithLocation(MutableLiveData<GoongPlaceDetailResult> liveData) {
        Call<GoongPlaceDetailResult> call =
                goongPlaceService.placeDetailWithPlaceId(
                        goongPlaceAutocompleteResult.getPredictions().get(0).getPlaceID(),
                        BuildConfig.GOONG_API_KEY);
        call.enqueue(new Callback<GoongPlaceDetailResult>() {
            @Override
            public void onResponse(Call<GoongPlaceDetailResult> call, Response<GoongPlaceDetailResult> response) {
                if (response.isSuccessful()){
                    GoongPlaceDetailResult result = response.body();
                    if (result != null){
                        liveData.postValue(result);
                    }

                }else{
                    liveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GoongPlaceDetailResult> call, Throwable t) {
                Log.d("GoongPlaceRepository", "onFailure: " + t.getMessage());
            }
        });
    }

    public void getPlaceAutoCompleteWithLocation(
            @NonNull Location location,
            MutableLiveData<GoongPlaceDetailResult> goongPlaceDetailResultMutableLiveData) {
        String locationString = location.getLat() + ",%20" + location.getLng();

        Call<GoongPlaceAutocompleteResult> call =
                goongPlaceService.placeAutocompleteWithLocation(
                        BuildConfig.GOONG_API_KEY, locationString);
        call.enqueue(new Callback<GoongPlaceAutocompleteResult>() {
            @Override
            public void onResponse(Call<GoongPlaceAutocompleteResult> call, Response<GoongPlaceAutocompleteResult> response) {
                if (response.isSuccessful()){
                    goongPlaceAutocompleteResult = response.body();
                    getPlaceDetailWithLocation(goongPlaceDetailResultMutableLiveData);
                }else{
                    goongPlaceAutocompleteResult = null;
                }
            }

            @Override
            public void onFailure(Call<GoongPlaceAutocompleteResult> call, Throwable t) {
                Log.d("GoongPlaceRepository", "onFailure: " + t.getMessage());
            }
        });
    }

}
