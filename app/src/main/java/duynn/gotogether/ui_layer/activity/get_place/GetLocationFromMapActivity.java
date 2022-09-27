package duynn.gotogether.ui_layer.activity.get_place;

import static duynn.gotogether.domain_layer.common.Constants.placeFields;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import duynn.gotogether.BuildConfig;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityGetLocationFromMapBinding;

public class GetLocationFromMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = GetLocationFromMapActivity.class.getSimpleName();
    private GoogleMap mMap;
    private ActivityGetLocationFromMapBinding binding;

    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGetLocationFromMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        placesClient = Places.createClient(this);

        initOnClick();
    }

    private void initOnClick() {
        //close button
        binding.getLocationFromMapClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
        //done button
        binding.getLocationFromMapDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng center = mMap.getCameraPosition().target;
                // Add a marker in Sydney and move the camera
                mMap.addMarker(new MarkerOptions().position(center).title("Pin location"));

                Place place = getLocationDetail(center);
                sendDataToGetPlaceActivity(place);

            }
        });
    }

    private Place getLocationDetail(LatLng center) {
        Place place = null;
//        placesClient.

        return place;
    }

    private void sendDataToGetPlaceActivity(Place place) {
        Log.d(TAG, "sendDataToGetPlaceActivity: " + place.getName());
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


//        LatLng center = mMap.getCameraPosition().target;
//
//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(center).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}