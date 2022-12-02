package duynn.gotogether.ui_layer.activity.execute_route;

import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.FirebaseMessageRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import duynn.gotogether.domain_layer.common.Constants;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TrackingMapsViewModel extends AndroidViewModel {
    MutableLiveData<Trip> trip;
    MutableLiveData<List<ClientTrip>> clientTrips;
    MutableLiveData<Bundle> bundle;
    MutableLiveData<Long> startTime;
    MutableLiveData<Long> endTime;
    TripRepo tripRepo;
    FirebaseMessageRepo firebaseMessageRepo;
    SessionManager sessionManager;
    MutableLiveData<String> message;
    MutableLiveData<String> status;
    MutableLiveData<Map<String,Double>> mapDistance;

    public TrackingMapsViewModel(@NonNull @NotNull Application application) {
        super(application);
        trip = new MutableLiveData<>();
        clientTrips = new MutableLiveData<>();
        bundle = new MutableLiveData<>();
        startTime = new MutableLiveData<>();
        endTime = new MutableLiveData<>();
        trip.setValue(new Trip());
        clientTrips.setValue(new ArrayList<>());
        bundle.setValue(new Bundle());
        startTime.setValue(0L);
        endTime.setValue(0L);
        sessionManager = SessionManager.getInstance(application);
        tripRepo = TripRepo.getInstance(sessionManager.getToken());
        firebaseMessageRepo = FirebaseMessageRepo.getInstance(getApplication());
        mapDistance = new MutableLiveData<>();
        Map<String,Double> map = new HashMap<>();
        map.put("my location",0.0);
        mapDistance.setValue(map);
        status = new MutableLiveData<>();
        status.setValue("");
        message = new MutableLiveData<>();
        message.setValue("");
    }

    public void requestFinishPassenger(int position, Long clientTripId) {
        ClientTrip clientTrip = Objects.requireNonNull(clientTrips.getValue()).get(position);
        //get data
        Double distance = 10.0;
        Double startDis = mapDistance.getValue()
                .getOrDefault(String.valueOf(clientTrip.getClient().getLocation().getId()),0.0);
        Double endDis = mapDistance.getValue().get(Constants.LAST_DISTANCE);
        distance = endDis - startDis;

        Trip trip = this.trip.getValue();
        //send notification
        firebaseMessageRepo.requestFinishPassenger(clientTrip,
                clientTrip.getNumOfPeople(),
                distance,
                distance*trip.getPricePerKm(),
                clientTripId,
                sessionManager.getClient().getId(),
                status, message);
    }

    public void finishTrip() {
        tripRepo.finishTrip(trip.getValue().getId(), status, message);
    }

    public void sendNotification(String geofenceId) {
        Trip trip = this.trip.getValue();
        for(ClientTrip clientTrip : clientTrips.getValue()) {
            Place endPlace = clientTrip.getDropOffPlace();
            Client client = clientTrip.getClient();
            if (endPlace != null) {
                if (endPlace.getName().equals(geofenceId)) {
                    //getdata
                    Map<String, Double> map = mapDistance.getValue();
                    Double distance = 53.333333;
                    Double startDis = map.get(clientTrip.getPickUpPlace().getName());
                    Double endDis = map.get(clientTrip.getDropOffPlace().getName());
                    distance = endDis - startDis;
                    //send notification
                    firebaseMessageRepo.requestFinishPassenger(clientTrip,
                            clientTrip.getNumOfPeople(),
                            distance,
                            distance*trip.getPricePerKm(),
                            clientTrip.getId(),
                            trip.getDriver().getId(),
                            status, message);
                }
            }
        }
    }

    public void updateDistance(String key, Double newDistance) {
        Map<String,Double> map = mapDistance.getValue();
        if(map!=null)
            map.putIfAbsent(key, newDistance);
        Log.d("updateDistance",map.toString());
        mapDistance.setValue(map);
    }
    public void updateDistance(Double newDistance) {
        Map<String,Double> map = mapDistance.getValue();
        map.put(Constants.LAST_DISTANCE,newDistance);
        Log.d("updateDistance",map.toString());
        mapDistance.setValue(map);
    }

    public Long getTime() {
        return null;
    }
}
