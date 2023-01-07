package duynn.gotogether.ui_layer.activity.search;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientDTO;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripDTO;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.ClientTripResponse;
import duynn.gotogether.data_layer.model.dto.client_trip_dto.TripDTO;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.domain_layer.DistanceUseCase;
import org.jetbrains.annotations.NotNull;

public class SearchResultViewModel extends AndroidViewModel {
    MutableLiveData<String> status;
    MutableLiveData<String> message;
    SessionManager sessionManager;
    MutableLiveData<ClientTrip> clientTripResponse;
    ClientTripRepo clientTripRepo;

    public SearchResultViewModel(@NonNull @NotNull Application application) {
        super(application);
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        clientTripResponse = new MutableLiveData<>();
        sessionManager = SessionManager.getInstance(application);
        status.setValue("loading");
        message.setValue("Loading...");
        clientTripResponse.setValue(new ClientTrip());

        String token = sessionManager.getToken();
        clientTripRepo = ClientTripRepo.getInstance(token);
    }

    public void regitTrip(Long tripId, Long clientId, ClientTrip searchTripRequest) {
        ClientTrip clientTrip = new ClientTrip();
        //trip
        Trip tripDTO = Trip.builder().id(tripId).build();
        clientTrip.setTrip(tripDTO);
        //client
        Client clientDTO = new Client();
        clientDTO.setId(clientId);
        clientTrip.setClient(clientDTO);
        //from to
        clientTrip.setPickUpPlace(searchTripRequest.getPickUpPlace());
        clientTrip.setDropOffPlace(searchTripRequest.getDropOffPlace());
        //time
        clientTrip.setPickUpTime(searchTripRequest.getPickUpTime());
        //seat
        clientTrip.setNumOfPeople(searchTripRequest.getNumOfPeople());

        clientTripRepo.regitTrip(clientTrip, status, message, clientTripResponse);
    }

    public Double getEstimatedDistance(ClientTrip searchTripRequest) {
        LatLng start = new LatLng(searchTripRequest.getPickUpPlace().getLat(),
                searchTripRequest.getPickUpPlace().getLng());
        LatLng end = new LatLng(searchTripRequest.getDropOffPlace().getLat(),
                searchTripRequest.getDropOffPlace().getLng());
        return SphericalUtil.computeDistanceBetween(start, end)/1000;//to km
    }
}
