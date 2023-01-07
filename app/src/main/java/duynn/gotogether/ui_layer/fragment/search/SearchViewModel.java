package duynn.gotogether.ui_layer.fragment.search;

import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
public class SearchViewModel extends ViewModel {

    private static final String TAG = SearchViewModel.class.getSimpleName();
    private MutableLiveData<ClientTrip> searchClientTrip;
    private MutableLiveData<List<Trip>> listTrip;
    private MutableLiveData<String> status;
    public SearchViewModel() {
        searchClientTrip = new MutableLiveData<>();
        ClientTrip request = ClientTrip.builder()
                .pickUpTime(Calendar.getInstance())
                .numOfPeople(1)
                .build();
        searchClientTrip.setValue(request);
        status = new MutableLiveData<>();
        status.setValue("");
        listTrip = new MutableLiveData<>();
        listTrip.setValue(new ArrayList<>());
    }
    public void searchTrip(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getToken();
        TripRepo.getInstance(token).searchTrip(context, searchClientTrip, listTrip, status);
    }


}