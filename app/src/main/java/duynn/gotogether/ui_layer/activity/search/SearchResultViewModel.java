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
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.domain_layer.DistanceUseCase;
import org.jetbrains.annotations.NotNull;

public class SearchResultViewModel extends AndroidViewModel {
    MutableLiveData<String> status;
    MutableLiveData<String> message;
    SessionManager sessionManager;
    MutableLiveData<ClientTripResponse> clientTripResponse;
    ClientTripRepo clientTripRepo;

    public SearchResultViewModel(@NonNull @NotNull Application application) {
        super(application);
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        clientTripResponse = new MutableLiveData<>();
        sessionManager = SessionManager.getInstance(application);
        status.setValue("loading");
        message.setValue("Loading...");
        clientTripResponse.setValue(new ClientTripResponse());

        String token = sessionManager.getToken();
        clientTripRepo = ClientTripRepo.getInstance(token);
    }

    public void regitTrip(Long tripId, Long clientId, SearchTripRequest searchTripRequest) {
        ClientTripDTO clientTripDTO = new ClientTripDTO();
        //trip
        TripDTO tripDTO = TripDTO.builder().id(tripId).build();
        clientTripDTO.setTrip(tripDTO);
        //client
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(clientId);
        clientTripDTO.setClient(clientDTO);
        //from to
        clientTripDTO.setPickUpPlace(searchTripRequest.getStartPlace());
        clientTripDTO.setDropOffPlace(searchTripRequest.getEndPlace());
        //time
        clientTripDTO.setPickUpTime(searchTripRequest.getStartTime());
        //seat
        clientTripDTO.setNumOfPeople(searchTripRequest.getNumOfSeat());

        clientTripRepo.regitTrip(clientTripDTO, status, message, clientTripResponse);
    }

    public Double getEstimatedDistance(SearchTripRequest searchTripRequest) {
        LatLng start = new LatLng(searchTripRequest.getStartPlace().getGeometry().getLocation().getLat(),
                searchTripRequest.getStartPlace().getGeometry().getLocation().getLng());
        LatLng end = new LatLng(searchTripRequest.getEndPlace().getGeometry().getLocation().getLat(),
                searchTripRequest.getEndPlace().getGeometry().getLocation().getLng());
        return SphericalUtil.computeDistanceBetween(start, end)/1000;//to km
    }
}
