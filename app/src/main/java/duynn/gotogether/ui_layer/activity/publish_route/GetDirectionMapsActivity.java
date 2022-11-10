package duynn.gotogether.ui_layer.activity.publish_route;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityGetDirectionMapsBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.get_place.GetPlaceActivity;
import duynn.gotogether.ui_layer.state_holders.view_model.TripViewModel;


@AndroidEntryPoint
public class GetDirectionMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = GetDirectionMapsActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ActivityGetDirectionMapsBinding binding;
    private LocationManager locationManager;
    private EditText startLocation;

    private TripViewModel tripViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetDirectionMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //init google places
//        Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
//        PlacesClient placesClient = Places.createClient(this);

//        AutocompleteSupportFragment autocompleteSupportFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.start_point_get_direction);
////        autocompleteSupportFragment.setTypeFilter(TypeFilter.ADDRESS);
//        autocompleteSupportFragment.setCountries("VN");
//        autocompleteSupportFragment.setLocationBias(RectangularBounds.newInstance(
//                new LatLng(10.5, 106.5),
//                new LatLng(21.5, 109.5)));
//        autocompleteSupportFragment.setPlaceFields(
//                Arrays.asList(
//                        Place.Field.ID,
//                        Place.Field.NAME,
//                        Place.Field.LAT_LNG));
//        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onError(@NonNull Status status) {
//                Log.d(TAG, "onError: " + status.getStatusMessage());
//            }
//
//            @Override
//            public void onPlaceSelected(@NonNull Place place) {
//                Log.d(TAG, "onPlaceSelected: " + place.getName());
//            }
//        });


//        startLocation = binding.startPointGetDirection;

//        startLocationChange();

        onClickStartAndEndEditText();

        tripViewModel = new ViewModelProvider(this).get(TripViewModel.class);
//        observeTripViewModel();
    }

    private void onClickStartAndEndEditText() {
        binding.startPointGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDirectionMapsActivity.this, GetPlaceActivity.class);
                startActivityForResult(intent, Constants.GET_START_POINT);
            }
        });

        binding.endPointGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetDirectionMapsActivity.this, GetPlaceActivity.class);
                startActivityForResult(intent, Constants.GET_END_POINT);
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == Constants.GET_START_POINT && resultCode == RESULT_OK) {
//            Position position = (Position) data.getBundleExtra(Constants.Bundle).get(Constants.Position);
////            GeocodingResult result = (GeocodingResult) data
////                    .getBundleExtra(Constants.Bundle)
////                    .get(Constants.GeocodingResult);
//            tripViewModel.setStartLocation(position);
//            Log.d(TAG, "onActivityResult: " + position.getFullAddress());
//        } else if (requestCode == Constants.GET_END_POINT && resultCode == RESULT_OK) {
//            Position position = (Position) data.getBundleExtra(Constants.Bundle).get(Constants.Position);
////            GeocodingResult result = (GeocodingResult) data
////                    .getBundleExtra(Constants.Bundle)
////                    .get(Constants.GeocodingResult);
//            tripViewModel.setEndLocation(position);
//            Log.d(TAG, "onActivityResult: " + position.getFullAddress());
//        }
//    }
//
//    private void observeTripViewModel() {
//        tripViewModel.getStartLocation().observe(this, startLocation -> {
//            if (startLocation != null) {
//                binding.startPointGetDirection.setText(startLocation.getPrimaryAddress());
//            }
//        });
//        tripViewModel.getEndLocation().observe(this, endLocation -> {
//            if (endLocation != null) {
//                binding.endPointGetDirection.setText(endLocation.getPrimaryAddress());
//            }
//        });
//    }

    private void startLocationChange() {
        startLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER){
                    //execute our method for searching
                    geoLocate();

                }
                return false;
            }
        });
    }

    private void geoLocate() {
        String searchString = startLocation.getText().toString();
        Geocoder geocoder = new Geocoder(GetDirectionMapsActivity.this);
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchString, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(list.size() > 0){
            Address address = list.get(0);
            Log.d("GetDirectionMapsActivity", "geoLocate: found a location: " + address.toString());

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
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //move my location button to right center
        View locationButton = ((View) binding.getRoot().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 0, 0);
//        map.getUiSettings().

        //TODO: fix null pointer exception
        //get current location
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
//            @Override
//            public void onComplete(@NonNull Task<Location> task) {
//                Location location = task.getResult();
//                if(location != null){
//                    currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
//                }
//            }
//        });


        Location myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // Add a marker in Sydney and move the camera
        if(myLocation!=null){
            LatLng myLatLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLatLng).title("My location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLatLng, Constants.DEFAULT_ZOOM));
        }

    }
}