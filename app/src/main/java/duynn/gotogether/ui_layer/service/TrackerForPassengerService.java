package duynn.gotogether.ui_layer.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.*;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.model.LatLng;
import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.data_layer.model.dto.execute_trip.ClientLocationDTO;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.ClientRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.repository.TripRepo;
import duynn.gotogether.domain_layer.common.Constants;
import lombok.Getter;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AndroidEntryPoint
@Getter
public class TrackerForPassengerService extends LifecycleService {
    @Inject
    NotificationCompat.Builder notification;
    @Inject
    NotificationManager notificationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;
    private final String TAG = TrackerForPassengerService.class.getSimpleName();
    private MutableLiveData<Boolean> started;
    private MutableLiveData<Long> startTime;
    private MutableLiveData<Long> endTime;
    private MutableLiveData<List<LatLng>> locationList;
    private MutableLiveData<Client> driverLocation;
    private MutableLiveData<Client> passengerLocation;
    private String role;
    private Trip trip;
    private ClientTrip clientTrip;
    private SessionManager sessionManager;
    private TripRepo tripRepo;
    private ClientRepo clientRepo;
    private Map<String, Double> distanceMap;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public TrackerForPassengerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackerForPassengerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        Bundle bundle = intent.getBundleExtra(Constants.Bundle);
        if (bundle != null) {
//            role = bundle.getString(Constants.ROLE);
            trip = (Trip) bundle.getSerializable(Constants.TRIP);
            clientTrip = (ClientTrip) bundle.getSerializable(Constants.CLIENT_TRIP);
//            Log.d(TAG, "onBind: -trip " + trip);
//            Log.d(TAG, "onBind: -clientTrip " + clientTrips);
        }
        return binder;
    }

    public MutableLiveData<List<LatLng>> getLocationList() {
        return locationList;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        started = new MutableLiveData<>();
        startTime = new MutableLiveData<>();
        endTime = new MutableLiveData<>();
        startTime.postValue(0L);
        endTime.postValue(0L);
        started.postValue(false);
        locationList = new MutableLiveData<>();
        locationList.postValue(new ArrayList<>());
        sessionManager = SessionManager.getInstance(this);
        tripRepo = TripRepo.getInstance(sessionManager.getToken());
        clientRepo = ClientRepo.getInstance(sessionManager.getToken());
        driverLocation = new MutableLiveData<>();
        passengerLocation = new MutableLiveData<>();
        passengerLocation.postValue(
                Client.builder()
                        .id(sessionManager.getClient().getId())
                        .lat(0)
                        .lng(0)
                        .build());
        driverLocation.postValue(Client.builder()
                .id(0L)
                .lat(0)
                .lng(0)
                .build());
        distanceMap = new HashMap<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                List<Location> locations = locationResult.getLocations();
                for (Location location : locations) {
                    passengerLocation.postValue(Client.builder()
                            .lat(location.getLatitude())
                            .lng(location.getLongitude())
                            .build());
                    Log.d(TAG, "onLocationResult: " + location.getLatitude() + " " + location.getLongitude());
                }
                //TODO: update location to server
                Location lastLocation = locations.get(locations.size() - 1);
                Client clientLocationDTO = Client.builder()
                        .id(sessionManager.getClient().getId())
                        .lat(lastLocation.getLatitude())
                        .lng(lastLocation.getLongitude())
                        .build();

                //TODO:new passenger
//                tripRepo.updatePassengerLocation(
//                        clientLocationDTO,
//                        trip.getDriver().getId(),
//                        driverLocation);
                clientRepo.updatePassengerLocation(
                        clientLocationDTO,
                        trip.getDriver().getId(),
                        driverLocation);
            }
        };
        driverLocation.observe(this, location -> {
            if (location != null) {
                Location driver = new Location("");
                driver.setLatitude(location.getLat());
                driver.setLongitude(location.getLng());
                updateLocationList(driver);
            }
        });

    }

    private void updateLocationList(Location location) {
        List<LatLng> list = locationList.getValue();
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(new LatLng(location.getLatitude(), location.getLongitude()));
        locationList.postValue(list);//postValue is thread safe in background thread
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.getAction().equals(Constants.ACTION_SERVICE_START)) {
                Bundle bundle = intent.getBundleExtra(Constants.Bundle);
                if (bundle != null) {
                    role = bundle.getString(Constants.ROLE);
                    trip = (Trip) bundle.getSerializable(Constants.TRIP);
//                    List<ClientTrip> clientTrips = (List<ClientTrip>) bundle.getSerializable(Constants.LIST_CLIENT_TRIP);
                    Log.d(TAG, "onStartCommand: -trip " + trip);
//                    Log.d(TAG, "onStartCommand: -clientTrip " + clientTrips);
                }
                Log.d(TAG, "onStartCommand: ACTION_SERVICE_START");
                started.postValue(true);
                startForegroundService();
                startLocationUpdates();
            } else if (intent.getAction().equals(Constants.ACTION_SERVICE_STOP)) {
                Log.d(TAG, "onStartCommand: ACTION_SERVICE_STOP");
                started.postValue(false);
                stopForegroundService();
            } else {
                Log.d(TAG, "onStartCommand: else");
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void stopForegroundService() {
        removeLocationUpdates();
        notificationManager.cancel(Constants.TRACKER_NOTIFICATION_ID);
        stopForeground(true);
        stopSelf();
        endTime.postValue(System.currentTimeMillis());
    }

    private void removeLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    private void startForegroundService() {
        createNotificationChanel();
        startForeground(Constants.TRACKER_NOTIFICATION_ID, notification.build());
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(Constants.LOCATION_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(Constants.LOCATION_UPDATE_FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper());

        startTime.postValue(System.currentTimeMillis());
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    Constants.TRACKER_NOTIFICATION_CHANNEL_ID,
                    Constants.TRACKER_NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public MutableLiveData<Long> getStartTime() {
        return startTime;
    }

    public MutableLiveData<Long> getEndTime() {
        return endTime;
    }
}
