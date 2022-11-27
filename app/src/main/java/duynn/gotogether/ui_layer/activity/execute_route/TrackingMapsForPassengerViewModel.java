package duynn.gotogether.ui_layer.activity.execute_route;

import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.FirebaseMessageRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrackingMapsForPassengerViewModel extends AndroidViewModel {
    MutableLiveData<Trip> trip;
    MutableLiveData<ClientTrip> clientTrip;
    MutableLiveData<Bundle> bundle;
    MutableLiveData<Long> startTime;
    MutableLiveData<Long> endTime;
    TripRepo tripRepo;
    FirebaseMessageRepo firebaseMessageRepo;
    SessionManager sessionManager;
    MutableLiveData<String> message;
    MutableLiveData<String> status;
    public TrackingMapsForPassengerViewModel(@NonNull @NotNull Application application) {
        super(application);
        trip = new MutableLiveData<>();
        clientTrip = new MutableLiveData<>();
        bundle = new MutableLiveData<>();
        startTime = new MutableLiveData<>();
        endTime = new MutableLiveData<>();
        trip.setValue(new Trip());
        clientTrip.setValue(new ClientTrip());
        bundle.setValue(new Bundle());
        startTime.setValue(0L);
        endTime.setValue(0L);
        sessionManager = SessionManager.getInstance(application);
        tripRepo = TripRepo.getInstance(sessionManager.getToken());
    }

}
