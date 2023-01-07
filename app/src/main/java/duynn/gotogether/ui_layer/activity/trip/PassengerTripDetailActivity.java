package duynn.gotogether.ui_layer.activity.trip;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.model.model.TripStopPlace;
import duynn.gotogether.databinding.ActivityPassengerTripDetailBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.publish_route.StopPlaceRecyclerViewAdapter;
import duynn.gotogether.ui_layer.activity.rating.DetailRatingActivity;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PassengerTripDetailActivity extends AppCompatActivity {
    private ActivityPassengerTripDetailBinding binding;
    private StopPlaceRecyclerViewAdapter stopPlaceRecyclerViewAdapter;
    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassengerTripDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable(Constants.TRIP);
        }
        initStopPlaceRV();
        bindingData();
    }

    private void bindingData() {
        binding.driverName.setText("Tên: "+trip.getDriver().getFullNameString());
//        binding.driverRate.setText("Đánh giá: "+trip.getDriver().getRate());
        binding.ratingBar.setRating(trip.getDriver().getRate().floatValue());
        binding.transportNumber.setText("BKS: "+trip.getTransport().getLicensePlate());
        binding.tripDate.setText("Ngày đi: "+ CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
        binding.tripPrice.setText("Giá: "+trip.getPricePerKm()+" VND/km");
        binding.tripDes.setText("Mô tả: "+trip.getDescription());
        binding.startTitle.setText(trip.getStartPlace().getName());
        binding.startDescription.setText(trip.getStartPlace().getFormattedAddress());
        binding.endTitle.setText(trip.getEndPlace().getName());
        binding.endDescription.setText(trip.getEndPlace().getFormattedAddress());
        binding.detailRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: detail rating
                Intent intent = new Intent(PassengerTripDetailActivity.this,
                        DetailRatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.ID, trip.getDriver().getId());
                intent.putExtra(Constants.Bundle, bundle);
                startActivity(intent);
            }
        });
    }

    private void initStopPlaceRV() {
        stopPlaceRecyclerViewAdapter = new StopPlaceRecyclerViewAdapter();
        if(trip != null && trip.getListStopPlace() != null){
            stopPlaceRecyclerViewAdapter.setListItem(trip.getListStopPlace().stream().map(TripStopPlace::getPlace).collect(Collectors.toList()));
        }else{
            stopPlaceRecyclerViewAdapter.setListItem(new ArrayList<>());
        }
        binding.tripStopList.setAdapter(stopPlaceRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.tripStopList.setLayoutManager(linearLayoutManager);
    }
}