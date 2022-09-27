package duynn.gotogether.ui_layer.activity.execute_route;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityTrackingMapsBinding;
import duynn.gotogether.domain_layer.DistanceUseCase;
import duynn.gotogether.domain_layer.PermissionsUseCase;
import duynn.gotogether.domain_layer.TimeUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.service.TrackerService;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

@AndroidEntryPoint
public class TrackingMapsActivity extends FragmentActivity implements OnMapReadyCallback, EasyPermissions.PermissionCallbacks {

    private final String TAG = TrackingMapsActivity.class.getSimpleName();
    TrackerService trackerService;
    private long startTime;
    private long endTime;
    boolean mBound = false;
    private GoogleMap map;
    private ActivityTrackingMapsBinding binding;
    private LocationManager locationManager;
    /**
     * Connect to service
     */
    private List<LatLng> locationList;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            TrackerService.LocalBinder binder = (TrackerService.LocalBinder) service;
            trackerService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTrackingMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        binding.btnStartTracking.setOnClickListener(v -> {
            onStartBtnClick(v);
        });
        binding.btnStopTracking.setOnClickListener(v -> {
            onStopBtnClick(v);
        });

        // Bind to TrackerService
        Intent intent = new Intent(this, TrackerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);

        startTime = 0L;
        endTime = 0L;

    }



    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;

    }
    private void onStopBtnClick(View v) {
        stopForegroundService();
        binding.btnStopTracking.setVisibility(View.GONE);
        binding.btnStartTracking.setVisibility(View.VISIBLE);
    }

    private void onStartBtnClick(View v) {
        if (PermissionsUseCase.hasBackgroundLocationPermission(this)) {
            binding.btnStartTracking.setVisibility(View.INVISIBLE);
            binding.btnStartTracking.setEnabled(false);
            binding.btnStopTracking.setVisibility(View.VISIBLE);

            sendActionCommandToService(Constants.ACTION_SERVICE_START);
            observeTrackerService();
        } else {
            PermissionsUseCase.requestBackgroundLocationPermission(this);
        }
    }

    private void sendActionCommandToService(String action) {
        Intent intent = new Intent(this, TrackerService.class);
        intent.setAction(action);
        startService(intent);
    }

    private void stopForegroundService() {
        binding.btnStartTracking.setEnabled(false);
        sendActionCommandToService(Constants.ACTION_SERVICE_STOP);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            PermissionsUseCase.requestBackgroundLocationPermission(this);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMyLocationEnabled(true);
//        map.getUiSettings().

        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Add a marker in Sydney and move the camera
        LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        map.addMarker(new MarkerOptions().position(myLatLng).title("My location"));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
    }

    private void observeTrackerService() {
        //location
        MutableLiveData<List<LatLng>> locationListLiveData = trackerService.getLocationList();
        if (locationListLiveData != null) {
            locationListLiveData.observe(this, locations -> {
                if (locations != null) {
                    locationList = locations;
                    Log.d(TAG, "observeTrackerService: " + locationList.toString());
                    drawPolyline();
                    followPolyline();
                }
            });
        }
        //start time
        MutableLiveData<Long> startTimeLiveData = trackerService.getStartTime();
        if (startTimeLiveData != null) {
            startTimeLiveData.observe(this, time -> {
                if (time != null) {
                    startTime = time;
//                    Log.d(TAG, "observeTrackerService: " + startTime);
                }
            });
        }
        //end time
        MutableLiveData<Long> endTimeLiveData = trackerService.getEndTime();
        if (endTimeLiveData != null) {
            endTimeLiveData.observe(this, time -> {
                if (time != null) {
                    endTime = time;
//                    Log.d(TAG, "observeTrackerService: " + endTime);
                }
                if(time != 0L){
                    showBiggerPicture();
                }
            });
        }
    }

    private void showBiggerPicture() {
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (LatLng latLng : locationList) {
            bounds.include(latLng);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100), 2000, null);
    }

    private void displayResult(){
        String distance = DistanceUseCase.distanceToString(DistanceUseCase.calculateDistance(locationList));
        String time = TimeUseCase.stringElapsedTime(startTime, endTime);
        Bundle bundle = new Bundle();
        bundle.putString("distance", distance);
        bundle.putString("time", time);
    }

    private void drawPolyline() {
        if (locationList != null) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(locationList);
            polylineOptions.width(10f);
            polylineOptions.color(Color.BLUE);
            polylineOptions.jointType(JointType.ROUND);
            polylineOptions.startCap(new ButtCap());
            polylineOptions.endCap(new ButtCap());
            map.addPolyline(polylineOptions);
            Log.d(TAG, "drawPolyline: " + locationList.toString());
        }
    }

    private void followPolyline(){
        if(!locationList.isEmpty()){
            CameraUpdate cu = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(locationList.get(locationList.size() - 1))
                    .zoom(Constants.DEFAULT_ZOOM_FOR_TRACKER)
                    .build());

            map.animateCamera(cu, Constants.DEFAULT_ANIMATION_TIME, null);
        }
    }


}