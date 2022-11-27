package duynn.gotogether.ui_layer.fragment.search;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import lombok.Getter;

import java.util.Calendar;

@Getter
public class SearchViewModel extends ViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();
    private MutableLiveData<SearchTripRequest> searchTripRequest;
    private MutableLiveData<ListTripResponse> searchTripResponse;
    private MutableLiveData<String> status;
    public SearchViewModel() {
        searchTripRequest = new MutableLiveData<>();
        SearchTripRequest request = new SearchTripRequest();
        request.setStartTime(Calendar.getInstance());
        request.setNumOfSeat(1);
        searchTripRequest.setValue(request);
        status = new MutableLiveData<>();
        status.setValue("");
        searchTripResponse = new MutableLiveData<>();
        searchTripResponse.setValue(new ListTripResponse());
    }
    public void searchTrip(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getToken();
        SearchTripRequest request = searchTripRequest.getValue();
        TripRepo.getInstance(token).searchTrip(context, searchTripRequest, searchTripResponse , status);
    }


}