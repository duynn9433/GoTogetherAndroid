package duynn.gotogether.ui_layer.activity.execute_route;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.tasks.OnSuccessListener;
import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.direction_helpers.FetchURL;
import duynn.gotogether.data_layer.direction_helpers.TaskLoadedCallback;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.ActivityTrackingMapsBinding;
import duynn.gotogether.databinding.ActivityTrackingMapsForPassengerBinding;
import duynn.gotogether.domain_layer.*;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.service.TrackerForPassengerService;
import duynn.gotogether.ui_layer.service.TrackerService;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import java.util.ArrayList;
import java.util.List;

@AndroidEntryPoint
public class TrackingMapsForPassengerActivity extends FragmentActivity
        implements TaskLoadedCallback,OnMapReadyCallback, EasyPermissions.PermissionCallbacks {
    private final String TAG = TrackingMapsForPassengerActivity.class.getSimpleName();
    private TrackingMapsForPassengerViewModel viewModel;
    private TrackerForPassengerService trackerService;
    private GoogleMap map;
    private ActivityTrackingMapsForPassengerBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private Polyline directionPolyline;
    boolean mBound = false;
    private MarkerOptions startMarker, endMarker;

    /**
     * Connect to service
     */
    private List<LatLng> locationList;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingMapsForPassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TrackingMapsForPassengerViewModel.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                TrackerForPassengerService.LocalBinder binder = (TrackerForPassengerService.LocalBinder) service;
                trackerService = binder.getService();
                mBound = true;
            }
            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
            }
        };
        //init button
        initButton();
        // get data from bundle
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        viewModel.bundle.setValue(bundle);
        Log.d(TAG, "onCreate - bundle: " + bundle);
        if (bundle != null) {
            Trip trip = (Trip) bundle.getSerializable(Constants.TRIP);
            ClientTrip clientTrip = (ClientTrip) bundle.getSerializable(Constants.CLIENT_TRIP);
            viewModel.trip.setValue(trip);
            viewModel.clientTrip.setValue(clientTrip);
            Log.d(TAG, "onCreate - trip: " + trip.toString());
            Log.d(TAG, "onCreate - clientTrip: " + clientTrip.toString());
            //marker
            initMarker();
        }
        // Bind to TrackerService
        Intent intent = new Intent(this, TrackerForPassengerService.class);
        intent.putExtra(Constants.Bundle,viewModel.bundle.getValue());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.btnStartTracking.performClick();
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 10000);
    }

    private void initButton() {
        binding.btnStartTracking.setOnClickListener(v -> {
            onStartBtnClick(v);
        });
        binding.btnStopTracking.setOnClickListener(v -> {
            onStopBtnClick(v);
        });
        binding.call.setOnClickListener(v -> {
            //TODo:  call
//            Intent intent = new Intent(Intent.ACTION_DIAL);
//            intent.setData(Uri.parse("tel:" + viewModel.trip.getValue().getDriver().getContactInfomation().getPhoneNumber()));
//            startActivity(intent);
            Intent intent = new Intent(this, PassengerFinishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(Constants.DISTANCE, "53.33");
            bundle.putString(Constants.PRICE, "5000");
            bundle.putString(Constants.PASSENGER_NUM, "1");
            intent.putExtra(Constants.Bundle, bundle);
            startActivity(intent);

        });
    }

    private void initMarker() {
        Trip trip = viewModel.trip.getValue();
        duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location startLocation =
                trip.getStartPlace().getGeometry().getLocation();
        startMarker = new MarkerOptions().position(new LatLng(
                startLocation.getLat(), startLocation.getLng()
        )).title("Start");
        duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location endLocation =
                trip.getEndPlace().getGeometry().getLocation();
        endMarker = new MarkerOptions().position(new LatLng(
                endLocation.getLat(), endLocation.getLng()
        )).title("End");
    }


    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(connection);
//        mBound = false;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
        mBound = false;
    }

    private void onStopBtnClick(View v) {
        stopForegroundService();
        binding.btnStopTracking.setVisibility(View.GONE);
        binding.btnStartTracking.setVisibility(View.VISIBLE);
        displayResult();
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
        Intent intent = new Intent(this, TrackerForPassengerService.class);
        intent.putExtra(Constants.Bundle,viewModel.bundle.getValue());
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    LatLng myLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, 15));
                                }
                            }
                        });
        //direction
//        map.addMarker(startMarker);
//        map.addMarker(endMarker);
        Trip trip = viewModel.trip.getValue();
        String vehicle = getVehicle(trip);
        new FetchURL(TrackingMapsForPassengerActivity.this)
                .execute(GetDirectionUrlUseCase.getMultiStopDirectionUrl(
                        trip.getStartPlace(),
                        trip.getEndPlace(),
                        trip.getListStopPlace(), vehicle), vehicle);
    }

    private String getGoogleMapTravelMode(Trip trip) {
        String vehicle = Constants.GOOGLE_MAPS_TRAVEL_MODE_DRIVING;
        switch (trip.getTransport().getTransportType()){
            case CAR:
                vehicle = Constants.GOOGLE_MAPS_TRAVEL_MODE_DRIVING;
                break;
            case BIKE:
                vehicle = Constants.GOOGLE_MAPS_TRAVEL_MODE_BICYCLING;
                break;
            case WALKING:
                vehicle = Constants.GOOGLE_MAPS_TRAVEL_MODE_WALKING;
                break;
            default:
                vehicle = Constants.GOOGLE_MAPS_TRAVEL_MODE_DRIVING;
                break;
        }
        return vehicle;
    }
    private String getVehicle(Trip trip) {
        String vehicle = Constants.VEHICLE_BIKE;
        switch (trip.getTransport().getTransportType()){
            case CAR:
                vehicle = Constants.VEHICLE_CAR;
                break;
            case BIKE:
                vehicle = Constants.VEHICLE_BIKE;
                break;
            case TRUCK:
                vehicle = Constants.VEHICLE_TRUCK;
                break;
            case TAXI:
                vehicle = Constants.VEHICLE_TAXI;
                break;
        }
        return vehicle;
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
                    viewModel.startTime.postValue(time);
//                    Log.d(TAG, "observeTrackerService: " + startTime);
                }
            });
        }
        //end time
        MutableLiveData<Long> endTimeLiveData = trackerService.getEndTime();
        if (endTimeLiveData != null) {
            endTimeLiveData.observe(this, time -> {
                if (time != null) {
                    viewModel.endTime.postValue(time);
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
        Double distance =DistanceUseCase.calculateDistance(locationList);
        String distanceS = DistanceUseCase.formatToString2digitEndPoint(distance);
        String time = TimeUseCase.stringElapsedTime(viewModel.startTime.getValue(), viewModel.endTime.getValue());
        Bundle bundle = new Bundle();
        bundle.putString("distance", distanceS);
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


    @Override
    public void onTaskDone(Object... values) {
        if(directionPolyline != null){
            directionPolyline.remove();
        }
        directionPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}