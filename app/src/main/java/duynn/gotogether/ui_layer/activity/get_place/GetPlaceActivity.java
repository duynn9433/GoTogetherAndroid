package duynn.gotogether.ui_layer.activity.get_place;

import static duynn.gotogether.domain_layer.PermissionsUseCase.requestLocationPermission;
import static duynn.gotogether.domain_layer.common.Constants.placeFields;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.LocationBias;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import duynn.gotogether.BuildConfig;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.response.GoogleGeocoding.GeocodingResult;
import duynn.gotogether.databinding.ActivityGetPlaceBinding;
import duynn.gotogether.domain_layer.PermissionsUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;


public class GetPlaceActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private static final String TAG = GetPlaceActivity.class.getSimpleName();
    private Handler handler = new Handler();
    private PlacePredictionAdapter adapter = new PlacePredictionAdapter();
    private Gson gson = new GsonBuilder().registerTypeAdapter(LatLng.class, new LatLngAdapter())
            .create();

    private RequestQueue queue;
    private PlacesClient placesClient;
    private AutocompleteSessionToken sessionToken;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private ViewAnimator viewAnimator;
    private ProgressBar progressBar;

    private ActivityGetPlaceBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetPlaceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the SDK
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), BuildConfig.MAPS_API_KEY);
        }
        placesClient = Places.createClient(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //toolbar
        setSupportActionBar(binding.toolbar);

        //init view
        viewAnimator = binding.viewAnimator;
        progressBar = binding.progressBar;
        queue = Volley.newRequestQueue(this);
        initRecyclerView();

        //init button
        initButton();
    }

    private void initButton() {
        binding.getPlaceMyLocationBtn.setOnClickListener(v -> {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
//            Position position = getMyLocation();
//            bundle.putSerializable(Constants.Position, position);
            intent.putExtra(Constants.Bundle, bundle);
            setResult(RESULT_OK, intent);
            finish();
        });
        binding.getPlaceChooseFromMapBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetLocationFromMapActivity.class);
            startActivityForResult(intent, Constants.GET_LOCATION_FROM_MAP_GET_PLACE);
        });
    }

    /**Get current location of device*/
//    @SuppressLint("MissingPermission")
//    private Position getMyLocation() {
//        Position position = new Position();
//        position.setPrimaryAddress("My location");
//        position.setFormattedAddress("My location");
//        position.setFullAddress("My location");
//        if(PermissionsUseCase.hasLocationPermission(this)){
//            Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
//            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
//                @Override
//                public void onComplete(@NonNull Task<Location> task) {
//                    if (task.isSuccessful()) {
//                        // Set the map's camera position to the current location of the device.
//                        Location lastKnownLocation = task.getResult();
//                        if (lastKnownLocation != null) {
//                            position.setLatitude(lastKnownLocation.getLatitude());
//                            position.setLongitude(lastKnownLocation.getLongitude());
//                        }
//                    } else {
//                        Toast.makeText(GetPlaceActivity.this, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
//                        //default location is Hanoi
//                        position.setLatitude(21.028511);
//                        position.setLongitude(105.804817);
//                        Log.e(TAG, "Exception: %s", task.getException());
//                    }
//                }
//            });
//        }else{
//            PermissionsUseCase.requestLocationPermission(this);
//        }
//        return position;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //TODO: get my location
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }else{
            requestLocationPermission(this);
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /**Get location from map*/
//        if(requestCode == Constants.GET_LOCATION_FROM_MAP_GET_PLACE){
//            if(resultCode == RESULT_OK){
//                //get data
//                Bundle bundle = data.getBundleExtra(Constants.Bundle);
//                Position position = (Position) bundle.getSerializable(Constants.Position);
//                //send data
//                Intent intent = new Intent();
//                Bundle bundle1 = new Bundle();
//                bundle1.putSerializable(Constants.Position, position);
//                intent.putExtra(Constants.Bundle, bundle1);
//                setResult(RESULT_OK, intent);
//                finish();
//            }else{
//                Toast.makeText(this, "Không thể lấy vị trí ", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    /**Create recyclerview*/
    private void initRecyclerView() {
        final RecyclerView recyclerView = binding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
//        adapter.setPlaceClickListener(this::geocodePlaceAndDisplay);
        adapter.setPlaceClickListener(this::getPlaceDetail);
    }
    private void getPlaceDetail(AutocompletePrediction placePrediction){
        FetchPlaceRequest request = FetchPlaceRequest.builder(placePrediction.getPlaceId(), placeFields).build();
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place);
//            displayDialog(placePrediction,place);
//            Position position = new Position();
//            position.setPrimaryAddress(place.getName());
//            position.setFormattedAddress(place.getAddress());
//            position.setFullAddress(place.getAddress());
//            position.setLatitude(place.getLatLng().latitude);
//            position.setLongitude(place.getLatLng().longitude);
//            //send data
//            Intent intent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constants.Position, position);
//            intent.putExtra(Constants.Bundle, bundle);
//            setResult(RESULT_OK, intent);
//            finish();
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                int statusCode = apiException.getStatusCode();
            }
        });
    }


    /**
     * Performs a Geocoding API request and displays the result in a dialog.
     *
     * @see <a href="https://developers.google.com/maps/documentation/geocoding/intro">...</a>
     */
    private void geocodePlaceAndDisplay(AutocompletePrediction placePrediction) {
        // Construct the request URL
        final String apiKey = BuildConfig.MAPS_API_KEY;
        final String url = "https://maps.googleapis.com/maps/api/geocode/json?place_id=%s&key=%s";
//        Log.d(TAG, placePrediction.getPlaceId());
        final String requestURL = String.format(url, placePrediction.getPlaceId(), apiKey);

        // Use the HTTP request URL for Geocoding API to get geographic coordinates for the place
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, null,
                response -> {
                    try {
                        // Inspect the value of "results" and make sure it's not empty
                        JSONArray results = response.getJSONArray("results");
                        if (results.length() == 0) {
                            Log.w(TAG, "No results from geocoding request.");
                            return;
                        }

                        // Use Gson to convert the response JSON object to a POJO
                        Log.d(TAG, "1:"+ results.getJSONObject(0).toString());
                        Log.d(TAG, "2:" + results.getString(0));
                        GeocodingResult result = gson.fromJson(
                                results.getString(0), GeocodingResult.class);
                        //display result
//                        displayDialog(placePrediction,  result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.e(TAG, "Request failed"));

        // Add the request to the Request queue.
        queue.add(request);
    }

//    private void displayDialog(AutocompletePrediction placePrediction, Place place) {
//        Log.d(TAG, "displayDialog: " + place.toString());
//        new AlertDialog.Builder(this)
//                .setTitle(placePrediction.getPrimaryText(null))
//                .setMessage("Geocoding result:\n" + place.getLatLng().toString())
//                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Intent intent = new Intent();
//                        Bundle bundle = new Bundle();
//                        Position position = new Position();
//                        position.setLatitude(place.getLatLng().latitude);
//                        position.setLongitude(place.getLatLng().longitude);
//                        position.setFormattedAddress(place.getAddress());
//                        position.setFullAddress(placePrediction.getFullText(null).toString());
//                        position.setPrimaryAddress(placePrediction.getPrimaryText(null).toString());
//                        bundle.putSerializable(Constants.Position, position);
//
////                        result.formattedAddress = placePrediction.getPrimaryText(null).toString();
////                        bundle.putSerializable(Constants.GeocodingResult, (Serializable) result);
//
//                        intent.putExtra(Constants.Bundle, bundle);
//                        setResult(RESULT_OK, intent);
//                        finish();
//                    }
//                })
//                .setNegativeButton(android.R.string.cancel, null)
//                .show();
//    }

    /**Create menu*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.get_place_menu, menu);
        final SearchView searchView = (SearchView) menu.findItem(R.id.get_place_menu_search).getActionView();
        initSearchView(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    private void initSearchView(SearchView searchView) {
        searchView.setQueryHint(getString(R.string.search_a_place));
        searchView.setIconifiedByDefault(false);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setIndeterminate(true);

                // Cancel any previous place prediction requests
                handler.removeCallbacksAndMessages(null);

                // Start a new place prediction request in 300 ms
                handler.postDelayed(() -> {
                    getPlacePredictions(newText);
                }, 300);
                return true;
            }
        });
    }

    /**
     * This method demonstrates the programmatic approach to getting place predictions. The
     * parameters in this request are currently biased to Kolkata, India.
     *
     * @param query the plus code query string (e.g. "GCG2+3M K")
     */
    private void getPlacePredictions(String query) {

        // The value of 'bias' biases prediction results to the rectangular region provided
        // (currently Kolkata). Modify these values to get results for another area. Make sure to
        // pass in the appropriate value/s for .setCountries() in the
        // FindAutocompletePredictionsRequest.Builder object as well.
        //TODO: change to vietnam
        final LocationBias bias = RectangularBounds.newInstance(
                new LatLng(22.458744, 88.208162), // SW lat, lng
                new LatLng(22.730671, 88.524896) // NE lat, lng
        );

        // Create a new programmatic Place Autocomplete request in Places SDK for Android
        final FindAutocompletePredictionsRequest newRequest = FindAutocompletePredictionsRequest
                .builder()
                .setSessionToken(sessionToken)
                .setLocationBias(bias)
//                .setTypeFilter(TypeFilter.ESTABLISHMENT)
                .setQuery(query)
                .setCountries("VN")
                .build();

        // Perform autocomplete predictions request
        placesClient.findAutocompletePredictions(newRequest).addOnSuccessListener((response) -> {
            List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
            adapter.setPredictions(predictions);

            progressBar.setIndeterminate(false);
            viewAnimator.setDisplayedChild(predictions.isEmpty() ? 0 : 1);
        }).addOnFailureListener((exception) -> {
            progressBar.setIndeterminate(false);
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + apiException.getStatusCode());
            }
        });
    }

    /**Create new session when search*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.get_place_menu_search){
            sessionToken = AutocompleteSessionToken.newInstance();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}