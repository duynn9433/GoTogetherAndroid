package duynn.gotogether.ui_layer.activity.trip;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.model.model.TripStopPlace;
import duynn.gotogether.databinding.ActivityDriverTripDetailBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.publish_route.StopPlaceRecyclerViewAdapter;
import duynn.gotogether.ui_layer.activity.rating.DetailRatingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DriverTripDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ActivityDriverTripDetailBinding binding;
    private DriverTripDetailViewModel viewModel;
    private StopPlaceRecyclerViewAdapter stopPlaceRecyclerViewAdapter;
    private PassengerJoinRVAdapter passengerJoinRVAdapter;
    private PassengerWaitRVAdapter passengerWaitRVAdapter;

    private Trip trip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverTripDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            trip = (Trip) bundle.getSerializable(Constants.TRIP);
        }

        viewModel = new ViewModelProvider(this).get(DriverTripDetailViewModel.class);
        viewModel.trip.setValue(trip);
        initStopPlaceRV();
        initPassengerRecyclerView();
        binding.swipeRefershLayout.setOnRefreshListener(this);
        observeData();

    }

    private void initPassengerRecyclerView() {
        //get data from server
        viewModel.getClientTripList();
        //init adapter
        passengerJoinRVAdapter = new PassengerJoinRVAdapter(new ArrayList<>(), this);
        passengerWaitRVAdapter = new PassengerWaitRVAdapter(new ArrayList<>(), this);
        binding.passengerJoinedList.setAdapter(passengerJoinRVAdapter);
        binding.passengerWaitingList.setAdapter(passengerWaitRVAdapter);
        RecyclerView.LayoutManager layoutManagerJoin = new LinearLayoutManager(this);
        RecyclerView.LayoutManager layoutManagerWait = new LinearLayoutManager(this);
        binding.passengerJoinedList.setLayoutManager(layoutManagerJoin);
        binding.passengerWaitingList.setLayoutManager(layoutManagerWait);

        //set listener
        passengerWaitRVAdapter.setOnItemClickListener(new PassengerWaitRVAdapter.OnItemClickListener() {
            @Override
            public void onAcceptClick(View view, int position) {
                ClientTrip clientTrip = passengerWaitRVAdapter.getListClientTrip().get(position);
                //accept passenger
                viewModel.acceptPassenger(clientTrip);
                //remove passenger from waiting list
                passengerWaitRVAdapter.getListClientTrip().remove(position);
                //add passenger to joined list
                passengerJoinRVAdapter.getListClientTrip().add(clientTrip);

                passengerWaitRVAdapter.notifyDataSetChanged();
                passengerJoinRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteClick(View view, int position) {
                //delete passenger
                viewModel.rejectPassenger(passengerWaitRVAdapter.getListClientTrip().get(position));
                //remove passenger from waiting list
                passengerWaitRVAdapter.getListClientTrip().remove(position);
                passengerWaitRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onRatingDetail(View view, int position) {
                ClientTrip clientTrip = passengerWaitRVAdapter.getListClientTrip().get(position);
                Intent intent = new Intent(DriverTripDetailActivity.this, DetailRatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong(Constants.ID, clientTrip.getClient().getId());
                intent.putExtra(Constants.Bundle, bundle);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onRefresh() {
        viewModel.getClientTripList();
        binding.swipeRefershLayout.setRefreshing(false);
    }

    private void initStopPlaceRV() {
        stopPlaceRecyclerViewAdapter = new StopPlaceRecyclerViewAdapter();
        stopPlaceRecyclerViewAdapter.setListItem(trip.getListStopPlace().stream()
                .map(TripStopPlace::getPlace)
                .collect(Collectors.toList()));
        binding.tripStopList.setAdapter(stopPlaceRecyclerViewAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.tripStopList.setLayoutManager(linearLayoutManager);
        stopPlaceRecyclerViewAdapter.setItemClickListener(new StopPlaceRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUseCase.showShortToast(DriverTripDetailActivity.this, "Không thể xoá ở đây");
            }
        });
    }

    private void observeData() {
        viewModel.trip.observe(this, trip -> {
            if(trip != null) {
                if(trip.getDriver() != null) {
                    if(trip.getDriver().getFullNameString() != null)
                        binding.driverName.setText("Tên: "+trip.getDriver().getFullNameString());
                    if(trip.getDriver().getRate() != null)
                        binding.ratingBar.setRating(trip.getDriver().getRate().floatValue());
//                        binding.driverRate.setText("Đánh giá: "+trip.getDriver().getRate());

                }
                if(trip.getTransport() != null && trip.getTransport().getLicensePlate() != null)
                    binding.transportNumber.setText("BKS: "+trip.getTransport().getLicensePlate());
                if(trip.getStartTime() != null){
                    binding.tripDate.setText("Ngày đi: "+ CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
                }
                if(trip.getPricePerKm() != null) {
                    binding.tripPrice.setText("Giá: "+trip.getPricePerKm()+" VND/km");
                }
                if(trip.getDescription() != null) {
                    binding.tripDes.setText("Mô tả: "+trip.getDescription());
                }
                if(trip.getStartPlace()!=null){
                    binding.tripFrom.setText("Điểm đi: "+trip.getStartPlace().getName());
                }
                if(trip.getEndPlace()!=null){
                    binding.tripTo.setText("Điểm đến: "+trip.getEndPlace().getName());
                }
            }
        });
        viewModel.clientTrips.observe(this, clientTrips -> {
            List<ClientTrip> joinList = new ArrayList<>();
            List<ClientTrip> waitList = new ArrayList<>();
            for (ClientTrip clientTrip : clientTrips) {
                if (clientTrip.isAccepted()) {
                    joinList.add(clientTrip);
                } else {
                    waitList.add(clientTrip);
                }
            }
            passengerJoinRVAdapter.setListClientTrip(joinList);
            passengerWaitRVAdapter.setListClientTrip(waitList);
            passengerJoinRVAdapter.notifyDataSetChanged();
            passengerWaitRVAdapter.notifyDataSetChanged();
        });

        viewModel.status.observe(this, status -> {
            if(status.equals(Constants.SUCCESS) ) {
                ToastUseCase.showShortToast(this, "Thành công");
            } else if(status.equals(Constants.FAIL)) {
                ToastUseCase.showShortToast(this, "Không thành công " + viewModel.message.getValue());
            }
        });
    }

}