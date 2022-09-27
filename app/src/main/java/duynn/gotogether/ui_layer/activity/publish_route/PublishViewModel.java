package duynn.gotogether.ui_layer.activity.publish_route;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import duynn.gotogether.data_layer.model.model.Trip;

public class PublishViewModel extends ViewModel{
    MutableLiveData<Trip> tripMutableLiveData;
    MutableLiveData<Place> startLocation;
    MutableLiveData<Place> endLocation;

    public PublishViewModel() {
        tripMutableLiveData = new MutableLiveData<>();
        startLocation = new MutableLiveData<>();
        endLocation = new MutableLiveData<>();
        tripMutableLiveData.setValue(new Trip());
        startLocation.setValue(new Place());
        endLocation.setValue(new Place());
    }

    public MutableLiveData<Trip> getTripMutableLiveData() {
        return tripMutableLiveData;
    }

    public void setTripMutableLiveData(MutableLiveData<Trip> tripMutableLiveData) {
        this.tripMutableLiveData = tripMutableLiveData;
    }

    public MutableLiveData<Place> getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(MutableLiveData<Place> startLocation) {
        this.startLocation = startLocation;
    }

    public MutableLiveData<Place> getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(MutableLiveData<Place> endLocation) {
        this.endLocation = endLocation;
    }

    public void publishTrip() {



    }
}
