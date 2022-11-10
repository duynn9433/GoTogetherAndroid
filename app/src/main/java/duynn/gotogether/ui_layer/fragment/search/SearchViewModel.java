package duynn.gotogether.ui_layer.fragment.search;

import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.SearchTripResponse;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.TripRepo;
import lombok.Getter;

@Getter
public class SearchViewModel extends ViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();
    private MutableLiveData<SearchTripRequest> searchTripRequest;
    private MutableLiveData<SearchTripResponse> searchTripResponse;
    private MutableLiveData<String> status;
    public SearchViewModel() {
        searchTripRequest = new MutableLiveData<>();
        searchTripRequest.setValue(new SearchTripRequest());
        status = new MutableLiveData<>();
        status.setValue("");
        searchTripResponse = new MutableLiveData<>();
        searchTripResponse.setValue(new SearchTripResponse());
    }
    public void searchTrip(Context context) {
        SearchTripRequest request = searchTripRequest.getValue();
        TripRepo.getInstance().searchTrip(context, searchTripRequest, searchTripResponse , status);
    }


}