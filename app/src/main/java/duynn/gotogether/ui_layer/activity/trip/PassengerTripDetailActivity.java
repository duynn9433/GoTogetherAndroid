package duynn.gotogether.ui_layer.activity.trip;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.ActivityPassengerTripDetailBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.publish_route.StopPlaceRecyclerViewAdapter;

import java.util.ArrayList;

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
        binding.driverRate.setText("Đánh giá: "+trip.getDriver().getRate());
        binding.transportNumber.setText("BKS: "+trip.getTransport().getLicensePlate());
        binding.tripDate.setText("Ngày đi: "+ CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
        binding.tripPrice.setText("Giá: "+trip.getPricePerKm()+" VND/km");
        binding.tripDes.setText("Mô tả: "+trip.getDescription());
        binding.tripFrom.setText("Điểm đi: "+trip.getStartPlace().getName());
        binding.tripTo.setText("Điểm đến: "+trip.getEndPlace().getName());
    }

    private void initStopPlaceRV() {
        stopPlaceRecyclerViewAdapter = new StopPlaceRecyclerViewAdapter();
        if(trip != null && trip.getListStopPlace() != null){
            stopPlaceRecyclerViewAdapter.setListItem(trip.getListStopPlace());
        }else{
            stopPlaceRecyclerViewAdapter.setListItem(new ArrayList<>());
        }
        binding.tripStopList.setAdapter(stopPlaceRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.tripStopList.setLayoutManager(linearLayoutManager);
    }
}