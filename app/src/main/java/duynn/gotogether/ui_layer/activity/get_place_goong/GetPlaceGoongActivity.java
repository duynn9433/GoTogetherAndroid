package duynn.gotogether.ui_layer.activity.get_place_goong;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;
import duynn.gotogether.BuildConfig;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.Prediction;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Location;
import duynn.gotogether.data_layer.retrofit_client.GoongClient;
import duynn.gotogether.databinding.ActivityGetPlaceGoongBinding;
import duynn.gotogether.domain_layer.PermissionsUseCase;
import duynn.gotogether.domain_layer.common.Constants;

@AndroidEntryPoint
public class GetPlaceGoongActivity extends AppCompatActivity {

    private static final String TAG = GetPlaceGoongActivity.class.getSimpleName();
    GetPlaceGoongViewModel viewModel;
    GoongPlaceDetailResult goongPlaceDetailResult;
    GoongClient goongClient;
    private ActivityGetPlaceGoongBinding binding;
    private GetPlaceGoongAdapter recycleViewAdapter;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetPlaceGoongBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        goongClient = new GoongClient();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        initButtonMyLocation();
        initButtonChooseFromMap();
//        initButtonClose();
        initButtonSearch();

        initRecyclerView();
        initViewModel();

        binding.searchText.requestFocus();
    }

    private void initViewModel() {
        viewModel = new GetPlaceGoongViewModel();
        viewModel.getLiveData().observe(this, new Observer<List<Prediction>>() {
            @Override
            public void onChanged(List<Prediction> predictions) {
                if (predictions != null) {
                    recycleViewAdapter.setListItem(predictions);
                    recycleViewAdapter.notifyDataSetChanged();
                } else {
                    recycleViewAdapter.setListItem(null);
                    recycleViewAdapter.notifyDataSetChanged();
                    Toast.makeText(GetPlaceGoongActivity.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }
        });
//        viewModel.getPlaceAutoCompleteWithText("");

    }

    private void initRecyclerView() {
        RecyclerView recyclerView = binding.getPlaceGoongRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recycleViewAdapter = new GetPlaceGoongAdapter();
        recyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.setItemClickListener(this::onItemClick);
    }

    private void onItemClick(View view, int position) {
        goongClient.getGoongPlaceService().placeDetailWithPlaceId(
                recycleViewAdapter.getListItem().get(position).getPlaceID(),
                        BuildConfig.GOONG_API_KEY)
                .enqueue(new retrofit2.Callback<GoongPlaceDetailResult>() {
                    @Override
                    public void onResponse(retrofit2.Call<GoongPlaceDetailResult> call, retrofit2.Response<GoongPlaceDetailResult> response) {
                        if (response.isSuccessful()) {
                            goongPlaceDetailResult = response.body();
                            Log.d(TAG+ " " + position, "onItemClick: " + response.body().getResult().toString());
                            sendDataToActivity();
                        }
                    }

                    @Override
                    public void onFailure(retrofit2.Call<GoongPlaceDetailResult> call, Throwable t) {
                        Toast.makeText(GetPlaceGoongActivity.this, "Không tìm thấy thông tin chi tiết địa điểm", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void sendDataToActivity() {
//        Log.d(TAG, "sendDataToActivity: " + goongPlaceDetailResult.toString());
        Intent intent = new Intent();
        if(goongPlaceDetailResult != null){
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constants.GOONG_PLACE_DETAIL_RESULT, goongPlaceDetailResult);
            intent.putExtra(Constants.Bundle, bundle);
            setResult(RESULT_OK, intent);
        }else{
            setResult(RESULT_CANCELED, intent);
        }
        finish();
    }

    private void initButtonClose() {
//        binding.close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                setResult(RESULT_CANCELED, intent);
//                finish();
//            }
//        });
        binding.close.setOnClickListener(v -> {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
            finish();
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    private void initButtonSearch() {
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = binding.searchText.getText().toString();
                if (keyword.isEmpty()) {
                    binding.searchText.setError("Please enter a keyword");
                    return;
                }
                binding.searchText.setError(null);
                binding.searchText.clearFocus();
                binding.searchText.setText("");
                binding.searchText.clearFocus();
                //call api search with keyword
                if(!keyword.isEmpty() && viewModel != null){
                    viewModel.getPlaceAutoCompleteWithText(keyword);
                }
            }
        });
    }

    private void initButtonChooseFromMap() {
    }

    private void initButtonMyLocation() {
        binding.getMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyLocation();

            }
        });
    }

    private void getPlaceDetailWithLocation(Location location) {
        viewModel.goongPlaceDetailResult.observe(this, new Observer<GoongPlaceDetailResult>() {
            @Override
            public void onChanged(GoongPlaceDetailResult goongPlaceDetailResult) {
                if(goongPlaceDetailResult != null){
                    GetPlaceGoongActivity.this.goongPlaceDetailResult = goongPlaceDetailResult;
                    sendDataToActivity();
                }
            }
        });
        viewModel.getPlaceDetailWithLocation(location);
    }

    @SuppressLint("MissingPermission")
    private Location getMyLocation() {
        Location location = new Location();
        if(PermissionsUseCase.hasLocationPermission(this)){
            Task<android.location.Location> locationResult = fusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    if (task.isSuccessful()) {
                        android.location.Location lastKnownLocation = task.getResult();
                        if (lastKnownLocation != null) {
                            location.setLat(lastKnownLocation.getLatitude());
                            location.setLng(lastKnownLocation.getLongitude());
                            Log.d(TAG, "onComplete: " + location.toString());
                            getPlaceDetailWithLocation(location);
                        }
                    } else {
                        Toast.makeText(GetPlaceGoongActivity.this, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
                        //default location is Hanoi
                        location.setLat(21.028511);
                        location.setLng(105.804817);
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        }else{
            PermissionsUseCase.requestLocationPermission(this);
            Toast.makeText(GetPlaceGoongActivity.this, "Không thể lấy vị trí của bạn", Toast.LENGTH_SHORT).show();
        }
        return location;
    }
}