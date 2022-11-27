package duynn.gotogether.ui_layer.activity.publish_route;

import android.widget.AdapterView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.TransportWithoutOwner;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.ActivityPublishBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.get_place_goong.GetPlaceGoongActivity;
import duynn.gotogether.ui_layer.adapter.TransportSpinnerAdapter;

public class PublishActivity extends AppCompatActivity {

    private static final String TAG = PublishActivity.class.getSimpleName();
    private ActivityPublishBinding binding;
    private PublishViewModel publishViewModel;
    private StopPlaceRecyclerViewAdapter stopPlaceRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);

        initStartLocation();
        initEndLocation();
        initStartDate();
        initStopLocation();
        initCancelButton();
        initPublishButton();

        initPassengerNumberAndPrice();
        initTransportSpinner();

        observerViewModel();

    }

    private void initTransportSpinner() {
        Client client = SessionManager.getInstance(this).getClient();
        List<TransportWithoutOwner> transportList = client.getTransports();
        TransportSpinnerAdapter transportSpinnerAdapter = new TransportSpinnerAdapter(transportList,this);
        binding.transportSpinner.setAdapter(transportSpinnerAdapter);
        binding.transportSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransportWithoutOwner transport = transportList.get(position);
                Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                assert trip != null;
                trip.setTransport(transport);
                publishViewModel.getTripMutableLiveData().setValue(trip);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initPassengerNumberAndPrice() {
        binding.emptySeat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s = binding.emptySeat.getText().toString();
                    if (s.isEmpty()) {
//                    binding.emptySeat.setError("Không được để trống");
                    } else {
                        try {
                            Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                            assert trip != null;
                            trip.setEmptySeat(Integer.parseInt(s));
                            publishViewModel.getTripMutableLiveData().setValue(trip);
                        }catch (NumberFormatException e){
                            binding.emptySeat.setError("Số ghế trống phải là số");
                        }
                    }
                }
            }
        });

        binding.pricePerKm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s = binding.pricePerKm.getText().toString();
                    Log.d(TAG, "onFocusChange: " + s);
                    if (s.isEmpty()) {
//                    binding.pricePerKm.setError("Không được để trống");
                    } else {
                        try {
                            Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                            assert trip != null;
                            trip.setPricePerKm(Double.parseDouble(s));
                            Log.d(TAG, "onFocusChange-trip: " + trip.getPricePerKm());
                            publishViewModel.getTripMutableLiveData().setValue(trip);
                        } catch (NumberFormatException e) {
                            binding.emptySeat.setError("Giá tiền phải là số");
                        }
                    }
                }
            }
        });
    }

    private void observerViewModel() {
        publishViewModel.getTripMutableLiveData().observe(this, trip -> {
            binding.startPoint.setText(trip.getStartPlace().getFormattedAddress());
            binding.endPoint.setText(trip.getEndPlace().getFormattedAddress());

            binding.startDate.setText(
                    new SimpleDateFormat("dd-MM-yyyy  HH:mm")
                            .format(trip.getStartTime().getTime()));
            binding.emptySeat.setText(trip.getEmptySeat().toString());
            binding.pricePerKm.setText(trip.getPricePerKm().toString());
        });

        publishViewModel.getStatus().observe(this, status -> {
            if(status != ""){
                if (Objects.equals(status, Constants.SUCCESS)) {
                    Toast.makeText(this, "Đăng thành công", Toast.LENGTH_SHORT).show();
                    //TODO: hen gio bao thuc

                    finish();
                } else if (Objects.equals(status, Constants.FAIL)) {
                    String message = publishViewModel.message.getValue();
                    Toast.makeText(this, "Đăng thất bại: " + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initStartLocation() {
        binding.startPoint.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetPlaceGoongActivity.class);
            startActivityForResult(intent, Constants.START_LOCATION_REQUEST_CODE);
        });
    }

    private void initEndLocation() {
        binding.endPoint.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetPlaceGoongActivity.class);
            startActivityForResult(intent, Constants.END_LOCATION_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == Constants.START_LOCATION_REQUEST_CODE) {
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                Place place = ((GoongPlaceDetailResult) bundle
                        .getSerializable(Constants.GOONG_PLACE_DETAIL_RESULT))
                        .getResult();
                Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                assert trip != null;
                trip.setStartPlace(place);
//                Log.d(TAG, "onActivityResult: " + place.getFormattedAddress());
                Objects.requireNonNull(publishViewModel.getTripMutableLiveData()).setValue(trip);
            } else if (requestCode == Constants.END_LOCATION_REQUEST_CODE) {
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                Place place = ((GoongPlaceDetailResult) bundle
                        .getSerializable(Constants.GOONG_PLACE_DETAIL_RESULT))
                        .getResult();
                Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                assert trip != null;
                trip.setEndPlace(place);
                Objects.requireNonNull(publishViewModel.getTripMutableLiveData()).setValue(trip);
            } else if (requestCode == Constants.STOP_LOCATION_REQUEST_CODE){
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                Place place = ((GoongPlaceDetailResult) bundle
                        .getSerializable(Constants.GOONG_PLACE_DETAIL_RESULT))
                        .getResult();
                Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                assert trip != null;
                trip.getListStopPlace().add(place);
                Objects.requireNonNull(publishViewModel.getTripMutableLiveData()).setValue(trip);
            }
        }
    }

    private void initStartDate() {
        binding.startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = (dateView, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    TimePickerDialog.OnTimeSetListener timeSetListener = (timeView, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        Trip trip = publishViewModel.getTripMutableLiveData().getValue();
                        trip.setStartTime(calendar);
                        publishViewModel.getTripMutableLiveData().setValue(trip);
                    };
                    new TimePickerDialog(PublishActivity.this, timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true).show();
                };
                new DatePickerDialog(PublishActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void initStopLocation() {
        initRecyclerView();
        initAddStopLocationButton();
    }

    private void initRecyclerView() {
        stopPlaceRecyclerViewAdapter = new StopPlaceRecyclerViewAdapter();
        binding.stopLocationRecyclerView.setAdapter(stopPlaceRecyclerViewAdapter);
        binding.stopLocationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        stopPlaceRecyclerViewAdapter.setItemClickListener(this::onItemClick);
        //observer modelview
        publishViewModel.getTripMutableLiveData().observe(this, trip -> {
            stopPlaceRecyclerViewAdapter.setListItem(trip.getListStopPlace());
            stopPlaceRecyclerViewAdapter.notifyDataSetChanged();
        });

    }

    private void onItemClick(View view, int position) {
        //TODO:delete stop place
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xoá điểm dừng");
        builder.setMessage("Bạn muốn xoá điểm dừng "
                + publishViewModel.getTripMutableLiveData().getValue().getListStopPlace().get(position).getName() + " ?");
        builder.setPositiveButton("Đồng ý", (dialog, which) -> {
            Trip trip = publishViewModel.getTripMutableLiveData().getValue();
            assert trip != null;
            trip.getListStopPlace().remove(position);
            publishViewModel.getTripMutableLiveData().setValue(trip);
        });
        builder.setNegativeButton("Bỏ qua", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void initAddStopLocationButton() {
        binding.addStopLocation.setOnClickListener(v -> {
            Intent intent = new Intent(this, GetPlaceGoongActivity.class);
            startActivityForResult(intent, Constants.STOP_LOCATION_REQUEST_CODE);
        });
    }

    private void initPublishButton() {
        binding.publishButton.setOnClickListener(v -> {
            Trip trip = publishViewModel.getTripMutableLiveData().getValue();
            assert trip != null;
            if (trip.getStartPlace() == null || trip.getEndPlace() == null) {
                Toast.makeText(this, "Please choose start and end location", Toast.LENGTH_SHORT).show();
                return;
            }
            if (trip.getStartTime() == null) {
                Toast.makeText(this, "Please choose start time", Toast.LENGTH_SHORT).show();
                return;
            }
            if (trip.getEmptySeat() == null) {
                Toast.makeText(this, "Please choose empty seat", Toast.LENGTH_SHORT).show();
                return;
            }
            if (trip.getPricePerKm() == null) {
                Toast.makeText(this, "Please choose price per km", Toast.LENGTH_SHORT).show();
                return;
            }
            publishViewModel.publishTrip(this);
            Log.d(TAG, "initPublishButton: " + publishViewModel.tripMutableLiveData.getValue().toString());
        });
    }

    private void initCancelButton() {
        binding.cancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}