package duynn.gotogether.ui_layer.activity.execute_route;

import android.app.PendingIntent;
import android.content.*;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.location.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.SphericalUtil;
import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.helper.direction_helpers.FetchURL;
import duynn.gotogether.data_layer.helper.direction_helpers.TaskLoadedCallback;
import duynn.gotogether.data_layer.model.dto.execute_trip.ClientLocationDTO;
import duynn.gotogether.data_layer.model.model.*;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.ActivityTrackingMapsBinding;
import duynn.gotogether.domain_layer.*;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.client_trip.PassengerActivity;
import duynn.gotogether.ui_layer.activity.execute_route.geofence.GeofenceHelper;
import duynn.gotogether.ui_layer.service.TrackerService;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static duynn.gotogether.R.drawable.emoji_people;

@AndroidEntryPoint
public class TrackingMapsActivity extends FragmentActivity
        implements TaskLoadedCallback,OnMapReadyCallback,
        EasyPermissions.PermissionCallbacks, SharedPreferences.OnSharedPreferenceChangeListener {
    private final String TAG = TrackingMapsActivity.class.getSimpleName();
    private TrackingMapsViewModel viewModel;
    private TrackerService trackerService;
    private SessionManager sessionManager;
    private GoogleMap map;
    private ActivityTrackingMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private Polyline directionPolyline;
    boolean mBound = false;
    boolean isStart = false;
    private MarkerOptions startMarker, endMarker;
    private List<MarkerOptions> markerOptionsList;
    private List<Client> passengerLocationList;
    private Client driverLocation;
    private List<Marker> markerList;
    private List<Marker> passengerMarkerList;
    private List<Geofence> geofenceList;
    private List<Double> listDistance;
    private List<LatLng> locationList;
    private ServiceConnection connection;

    /**
     * init attribute
     * get data from intent
     * bind to service
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        isStart = false;
        super.onCreate(savedInstanceState);
        binding = ActivityTrackingMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TrackingMapsViewModel.class);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);
        sessionManager = SessionManager.getInstance(this);
        sessionManager.registerListener(this);
        markerOptionsList = new ArrayList<>();
        passengerLocationList = new ArrayList<>();
        passengerMarkerList = new ArrayList<>();
        markerList = new ArrayList<>();
        geofenceList = new ArrayList<>();
        listDistance = new ArrayList<>();
        connection = new ServiceConnection() {
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
        //init button
        initButton();
        // get data from bundle
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        viewModel.bundle.setValue(bundle);
//        Log.d(TAG, "onCreate - bundle: " + bundle);
        if (bundle != null) {
            Trip trip = (Trip) bundle.getSerializable(Constants.TRIP);
            List<ClientTrip>clientTrips = (ArrayList<ClientTrip>) bundle.getSerializable(Constants.LIST_CLIENT_TRIP);
            viewModel.trip.setValue(trip);
            viewModel.clientTrips.setValue(clientTrips);
//            Log.d(TAG, "onCreate - trip: " + trip.toString());
//            Log.d(TAG, "onCreate - clientTrips: " + clientTrips.toString());
            //marker
            initMarker();

        }
        // Bind to TrackerService
        Intent intent = new Intent(this, TrackerService.class);
        intent.putExtra(Constants.Bundle,viewModel.bundle.getValue());
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        //

    }

    /**
     * start activity
     * */
    @Override
    protected void onStart() {
        super.onStart();
//        binding.btnStartTracking.performClick();
//        binding.btnStartTracking.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.btnStartTracking.performClick();
                binding.progressBar.setVisibility(View.GONE);
            }
        }, 10000);
    }

    /**
     * remove geofence
     * unbind service
     * */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        sessionManager.unregisterListener(this);
        List<String> listId = new ArrayList<>();
        for(Geofence geofence: geofenceList){
            listId.add(geofence.getRequestId());
        }
        geofencingClient.removeGeofences(listId);
        unbindService(connection);
        mBound = false;
    }

    private void initGeoFence() {
        for(MarkerOptions markerOptions: markerOptionsList){
            addGeofence(markerOptions.getPosition(),Constants.GEOFENCE_RADIUS,markerOptions.getSnippet());
        }
    }
    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius, String id) {
        Geofence geofence = geofenceHelper.getGeofence(id, latLng, radius,
                Geofence.GEOFENCE_TRANSITION_ENTER
                        | Geofence.GEOFENCE_TRANSITION_DWELL
                        | Geofence.GEOFENCE_TRANSITION_EXIT);
        geofenceList.add(geofence);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                        if(isStart == false){
                            isStart = true;
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    binding.btnStartTracking.performClick();
                                    binding.progressBar.setVisibility(View.GONE);
                                }
                            }, 5000);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
//        addCircle(latLng, radius);
    }
    private void addCircle(LatLng latLng,float radius){
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(radius)
                .strokeColor(Color.argb(50, 70,70,70))
                .fillColor(Color.argb(100, 150,150,150))
                .strokeWidth(5);
        map.addCircle(circleOptions);
    }

    private void initButton() {
        binding.btnStartTracking.setOnClickListener(v -> {
            onStartBtnClick(v);
        });
        binding.btnStopTracking.setOnClickListener(v -> {
            onStopBtnClick(v);
        });
        binding.getDirection.setOnClickListener(v -> {
            //TODO: get direction
        });
        binding.passengerFinish.setOnClickListener(v -> {
            Intent intent = new Intent(this, PassengerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.LIST_CLIENT_TRIP,new ArrayList<>(viewModel.clientTrips.getValue()));
            intent.putExtra(Constants.Bundle,bundle);
            startActivityForResult(intent,Constants.PASSENGER_REQUEST_CODE);
        });
        binding.driverFinish.setOnClickListener(v -> {
            binding.btnStopTracking.performClick();
            viewModel.finishTrip();
        });
    }
    /**
     * receive data callback from activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode: " + requestCode);
        if (requestCode == Constants.FINISH_TRIP_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                stopForegroundService();
                finish();
            }
        } else if (requestCode == Constants.PASSENGER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                ClientTrip clientTrip = (ClientTrip) bundle.getSerializable(Constants.CLIENT_TRIP);
                int position = bundle.getInt(Constants.POSITION);
                //update distance
                Double newDistance = DistanceUseCase.calculateDistance(locationList);
                viewModel.updateDistance(newDistance);
                viewModel.requestFinishPassenger(position,clientTrip.getId());
            }
        }
    }
    /**
     * show the result of trip
     * */
    private void displayResult(){
        Double distance =DistanceUseCase.calculateDistance(locationList);
        String distanceS = DistanceUseCase.formatToString2digitEndPoint(distance);
        String time = TimeUseCase.stringElapsedTime(viewModel.startTime.getValue(), viewModel.endTime.getValue());
        Intent intent = new Intent(this, DriverFinishActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DISTANCE, distanceS);
        bundle.putString(Constants.TIME, time);
        int numPassenger = 0;
        for(ClientTrip clientTrip: viewModel.clientTrips.getValue()){
            numPassenger += clientTrip.getNumOfPeople();
        }
        bundle.putString(Constants.PASSENGER_NUM, String.valueOf(numPassenger));
        intent.putExtra(Constants.Bundle,bundle);
        startActivity(intent);
        startActivityForResult(intent, Constants.FINISH_TRIP_REQUEST_CODE);
    }

    /**
     * Marker for start/stop/end place
     */
    private void initMarker() {
        Trip trip = viewModel.trip.getValue();
        startMarker = new MarkerOptions().position(new LatLng(
                trip.getStartPlace().getLat(), trip.getStartPlace().getLng()
        )).title("Start").snippet(trip.getStartPlace().getName());
        markerOptionsList.add(startMarker);
        for(int i = 0 ;i<trip.getListStopPlace().size();i++){
            Place place = trip.getListStopPlace().get(i).getPlace();
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(
                    place.getLat(), place.getLng()
            )).title("Stop "+(i+1)).snippet(place.getName());
            markerOptionsList.add(markerOptions);
        }
        endMarker = new MarkerOptions().position(new LatLng(
                trip.getEndPlace().getLat(), trip.getEndPlace().getLng()
        )).title("End").snippet(trip.getEndPlace().getName());
        markerOptionsList.add(endMarker);
    }
    @Override
    protected void onStop() {
        super.onStop();
//        unbindService(connection);
//        mBound = false;

    }

    private void onStopBtnClick(View v) {
        stopForegroundService();
        binding.progressBar.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.progressBar.setVisibility(View.GONE);
                displayResult();
            }
        }, 5000);
    }

    private void onStartBtnClick(View v) {
        if (PermissionsUseCase.hasBackgroundLocationPermission(this)) {
            sendActionCommandToService(Constants.ACTION_SERVICE_START);
            observeTrackerService();
        } else {
            PermissionsUseCase.requestBackgroundLocationPermission(this);
        }
    }

    private void sendActionCommandToService(String action) {
        Intent intent = new Intent(this, TrackerService.class);
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
                                    // go to google map
                                    duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location myLocation =
                                            duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location
                                                    .builder()
                                                    .id(0L)
                                                    .lat(location.getLatitude())
                                                    .lng(location.getLongitude())
                                                    .build();
                                    Trip trip = viewModel.trip.getValue();
                                    String uri = GoogleMapUrlUseCase.getGoogleMapUrlWithWaypoints(
                                            myLocation,
                                            trip.getStartPlace(),
                                            trip.getEndPlace(),
                                            trip.getListStopPlace().stream().map(TripStopPlace::getPlace).collect(Collectors.toList()),
                                            getGoogleMapTravelMode(trip));
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
//                                    intent.setPackage("com.google.android.apps.maps");
                                    //TODO: go to google map
                                    startActivity(intent);
                                }
                            }
                        });
        //marker
        drawMarker();
        //geofence
//        initGeoFence();
        //direction
        Trip trip = viewModel.trip.getValue();
        String vehicle = getVehicle(trip);
        new FetchURL(TrackingMapsActivity.this)
                .execute(GetDirectionUrlUseCase.getMultiStopDirectionUrl(
                            trip.getStartPlace(),
                            trip.getEndPlace(),
                            trip.getListStopPlace().stream()
                                    .map(TripStopPlace::getPlace)
                                    .collect(Collectors.toList()),
                            vehicle)
                        , vehicle);
    }

    private void drawMarker() {
        for(MarkerOptions markerOptions : markerOptionsList){
            Marker marker = map.addMarker(markerOptions);
            markerList.add(marker);
            addCircle(markerOptions.getPosition(), Constants.GEOFENCE_RADIUS);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        initGeoFence();
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
//                    Log.d(TAG, "observeTrackerService: " + locationList.toString());
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
        //passenger location
        MutableLiveData<List<Client>> passengerLocationLiveData = trackerService.getPassengerLocation();
        if (passengerLocationLiveData != null) {
            passengerLocationLiveData.observe(this, passengerLocations -> {
                if (passengerLocations != null) {
                    passengerLocationList = passengerLocations;
                    reloadDrawMarker();
                }
            });
        }
        //driver location
        MutableLiveData<Client> driverLocationLiveData = trackerService.getDriverLocation();
        if (driverLocationLiveData != null) {
            driverLocationLiveData.observe(this, location -> {
                if (location != null) {
                    driverLocation = location;
                }
            });
        }
    }

    private void reloadDrawMarker() {
        for(Marker marker : passengerMarkerList){
            marker.remove();
        }
        for(int i = 0; i < passengerLocationList.size(); i++){
            Client location = passengerLocationList.get(i);
            LatLng latLng = new LatLng(location.getLat(), location.getLng());
            Marker marker = map.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .icon(bitmapDescriptorFromVector(this, emoji_people)));
            passengerMarkerList.add(marker);
            if(driverLocation != null){
                Double distance = SphericalUtil.computeDistanceBetween(
                        latLng,
                        new LatLng(driverLocation.getLat(), driverLocation.getLng()));
                if(distance < Constants.GEOFENCE_RADIUS){
                    //update data
                    updateDistance(String.valueOf(location.getId()));
                }
            }
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    private void showBiggerPicture() {
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        for (LatLng latLng : locationList) {
            bounds.include(latLng);
        }
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 100), 2000, null);
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
//            Log.d(TAG, "drawPolyline: " + locationList.toString());
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(SessionManager.GEOFENCE)){
            String geofenceId = sharedPreferences.getString(Constants.GEOFENCE, "");
            //update list distance
            updateDistance(geofenceId);
            Log.d(TAG, "onSharedPreferenceChanged: " + geofenceId);
            viewModel.sendNotification(geofenceId);
        }
    }
    private void updateDistance(String key) {
        Double newDistance = DistanceUseCase.calculateDistance(locationList);
        viewModel.updateDistance(key, newDistance);
    }
}