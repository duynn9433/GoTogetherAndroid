package duynn.gotogether.ui_layer.service;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.*;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.domain_layer.common.Constants;

@AndroidEntryPoint
public class TrackerService extends LifecycleService {
    @Inject
    NotificationCompat.Builder notification;

    @Inject
    NotificationManager notificationManager;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private final String TAG = TrackerService.class.getSimpleName();
    private MutableLiveData<Boolean> started;
    private MutableLiveData<Long> startTime;
    private MutableLiveData<Long> endTime;
    private MutableLiveData<List<LatLng>> locationList;

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        public TrackerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return TrackerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for(Location location : locationResult.getLocations()){
                    updateLocationList(location);
                    Log.d(TAG, "onLocationResult: " + location.getLatitude() + " " + location.getLongitude());
                }
            }
        };

    }

    private void updateLocationList(Location location){
        List<LatLng> list = locationList.getValue();
        if(list == null){
            list = new ArrayList<>();
        }
        list.add(new LatLng(location.getLatitude(), location.getLongitude()));
        locationList.postValue(list);//postValue is thread safe in background thread
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if(intent.getAction().equals(Constants.ACTION_SERVICE_START)){
            Log.d(TAG, "onStartCommand: ACTION_SERVICE_START");
            started.postValue(true);
            startForegroundService();
            startLocationUpdates();
        }else if(intent.getAction().equals(Constants.ACTION_SERVICE_STOP)){
            Log.d(TAG, "onStartCommand: ACTION_SERVICE_STOP");
            started.postValue(false);
            stopForegroundService();
        }else{
            Log.d(TAG, "onStartCommand: else");
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


    private void startForegroundService(){
        createNotificationChanel();
        startForeground(Constants.TRACKER_NOTIFICATION_ID, notification.build());
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates(){
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

    private void createNotificationChanel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
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
