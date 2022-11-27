package duynn.gotogether.ui_layer.fragment.your_rides;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;

import java.util.ArrayList;
import java.util.List;

public class YourRidesViewModel extends AndroidViewModel {
    MutableLiveData<String> status;
    MutableLiveData<String> message;
    //for driver
    MutableLiveData<Trip> currentTrip;
    MutableLiveData<List<ClientTrip>> clientTrips;
    //for passenger
    MutableLiveData<Trip> acceptedTrip;
    MutableLiveData<ClientTrip> currentClientTrip;
    MutableLiveData<List<Trip>> waitingTrips;

    private TripRepo tripRepo;
    private SessionManager sessionManager;


    public YourRidesViewModel(@NonNull Application application) {
        super(application);
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        currentTrip = new MutableLiveData<>();
        clientTrips = new MutableLiveData<>();
        clientTrips.setValue(new ArrayList<>());
        sessionManager = SessionManager.getInstance(application);
        status.setValue("");
        message.setValue("");
        currentTrip.setValue(new Trip());
        tripRepo = TripRepo.getInstance(sessionManager.getToken());
        acceptedTrip = new MutableLiveData<>();
        acceptedTrip.setValue(new Trip());
        waitingTrips = new MutableLiveData<>();
        waitingTrips.setValue(new ArrayList<>());
        currentClientTrip = new MutableLiveData<>();
        currentClientTrip.setValue(new ClientTrip());
    }

    public void getCurrentTrip() {
        Client client = sessionManager.getClient();
        if (client != null) {
            tripRepo.getCurrentTrip(status, message, currentTrip, client.getId());
        }
    }

    public void startTrip() {
        tripRepo.startTrip(status, message, currentTrip, clientTrips);
    }

    public void getAcceptedTrip() {
        Client client = sessionManager.getClient();
        if (client != null) {
            tripRepo.getAcceptedTrip(status, message, acceptedTrip, client.getId());
        }
    }

    public void getWaitingTrips() {
        Client client = sessionManager.getClient();
        if (client != null) {
            tripRepo.getWaitingTrips(status, message, waitingTrips, client.getId());
        }
    }

    public void cancelTrip(Trip trip) {
        Client client = sessionManager.getClient();
        if (client != null) {
            tripRepo.cancelTrip(status, message, trip, client.getId());
        }
    }
}