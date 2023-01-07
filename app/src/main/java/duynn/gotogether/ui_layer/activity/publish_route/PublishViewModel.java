package duynn.gotogether.ui_layer.activity.publish_route;


import android.content.Context;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import duynn.gotogether.data_layer.model.model.Place;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublishViewModel extends ViewModel{
    private static final String TAG = PublishViewModel.class.getSimpleName();
    MutableLiveData<Trip> tripMutableLiveData;
//    MutableLiveData<Place> startLocation;
//    MutableLiveData<Place> endLocation;

    MutableLiveData<String> status;
    MutableLiveData<String> message;

    public PublishViewModel() {
        tripMutableLiveData = new MutableLiveData<>();
//        startLocation = new MutableLiveData<>();
//        endLocation = new MutableLiveData<>();
        tripMutableLiveData.setValue(new Trip());
//        startLocation.setValue(new Place());
//        endLocation.setValue(new Place());
        status = new MutableLiveData<>();
        status.setValue("");
        message = new MutableLiveData<>();
        message.setValue("");
    }

    public void publishTrip(Context context) {
        SessionManager sessionManager = SessionManager.getInstance(context);
        String token = sessionManager.getToken();

        Trip trip = tripMutableLiveData.getValue();
        TripRepo.getInstance(token).publish(trip, tripMutableLiveData, status, message, context);
    }
}
