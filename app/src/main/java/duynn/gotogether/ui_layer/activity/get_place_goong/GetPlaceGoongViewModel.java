package duynn.gotogether.ui_layer.activity.get_place_goong;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.InstallIn;
import dagger.hilt.android.lifecycle.HiltViewModel;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.Prediction;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.repository.GoongPlaceRepository;
import duynn.gotogether.data_layer.retrofit_client.GoongClient;
import duynn.gotogether.data_layer.service.GoongPlaceService;
import retrofit2.Retrofit;

public class GetPlaceGoongViewModel extends ViewModel {

    MutableLiveData<List<Prediction>> liveData;

    MutableLiveData<GoongPlaceDetailResult> goongPlaceDetailResult;

    GoongClient goongClient;

    public GetPlaceGoongViewModel() {
        liveData = new MutableLiveData<>();
        goongPlaceDetailResult = new MutableLiveData<>();
        goongClient = GoongClient.getInstance();
    }

    public MutableLiveData<List<Prediction>> getLiveData() {
        return liveData;
    }

    public void getPlaceAutoCompleteWithText(String input){
        GoongPlaceRepository goongPlaceRepository
                = new GoongPlaceRepository(goongClient.getGoongPlaceService());
        goongPlaceRepository.getPlaceAutoCompleteWithText(input, liveData);
    }

    public void getPlaceDetail(String placeId){
        GoongPlaceRepository goongPlaceRepository
                = new GoongPlaceRepository(goongClient.getGoongPlaceService());
        goongPlaceRepository.getPlaceDetail(placeId, goongPlaceDetailResult);
    }

    public void getPlaceDetailWithLocation(Location location){
        GoongPlaceRepository goongPlaceRepository
                = new GoongPlaceRepository(goongClient.getGoongPlaceService());
        goongPlaceRepository.getPlaceAutoCompleteWithLocation(location, goongPlaceDetailResult);
    }
}
