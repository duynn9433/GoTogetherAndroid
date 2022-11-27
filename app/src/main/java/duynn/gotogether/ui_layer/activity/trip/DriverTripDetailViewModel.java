package duynn.gotogether.ui_layer.activity.trip;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.config.di.ModelMapperConfig;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverTripDetailViewModel extends AndroidViewModel {
    MutableLiveData<String> status;
    MutableLiveData<String> message;
    MutableLiveData<Trip> trip;
    MutableLiveData<List<ClientTrip>> clientTrips;

    ClientTripRepo clientTripRepo;
    SessionManager sessionManager;

    public DriverTripDetailViewModel(@NonNull @NotNull Application application) {
        super(application);
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        trip = new MutableLiveData<>();
        clientTrips = new MutableLiveData<>();
        status.setValue("");
        message.setValue("");
        trip.setValue(new Trip());
        clientTrips.setValue(new ArrayList<>());

        sessionManager = SessionManager.getInstance(application);
        clientTripRepo = ClientTripRepo.getInstance(sessionManager.getToken());
    }

    public void getClientTripList() {
        clientTripRepo.getAll(status, message, clientTrips, Objects.requireNonNull(trip.getValue()).getId());
    }

    public void acceptPassenger(ClientTrip clientTrip) {
        clientTrip.setAccepted(true);
        clientTrip.setCanceled(false);
        clientTripRepo.update(status, message, clientTrip);
    }

    public void rejectPassenger(ClientTrip clientTrip) {
        clientTrip.setAccepted(false);
        clientTrip.setCanceled(true);
        clientTripRepo.update(status, message, clientTrip);
    }
}
