package duynn.gotogether.ui_layer.fragment.your_rides;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.FragmentProfileBinding;
import duynn.gotogether.databinding.FragmentYourRidesBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsActivity;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsForPassengerActivity;
import duynn.gotogether.ui_layer.activity.rating.UnratedTripActivity;
import duynn.gotogether.ui_layer.activity.search.SearchResultActivity;
import duynn.gotogether.ui_layer.activity.trip.DriverTripDetailActivity;
import duynn.gotogether.ui_layer.fragment.profile.ProfileViewModel;

import java.util.ArrayList;
import java.util.List;

public class YourRidesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = YourRidesFragment.class.getSimpleName();
    private YourRidesViewModel mViewModel;
    private FragmentYourRidesBinding binding;
    private WaitingTripRVAdapter waitingTripRVAdapter;

    public static YourRidesFragment newInstance() {
        return new YourRidesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentYourRidesBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(YourRidesViewModel.class);
        //get data
        mViewModel.getAcceptedTrip();
        mViewModel.getWaitingTrips();
        //        initRecyclerView();
        mViewModel.getCurrentTrip();
        observeData();
        initButton();
        binding.swipeRefershLayout.setOnRefreshListener(this);
        binding.publicTrip.setVisibility(View.GONE);
        binding.noPublicTrip.setVisibility(View.VISIBLE);
        binding.noRegitTrip.setVisibility(View.VISIBLE);
//        initAddTransport();
        return binding.getRoot();
    }

    private void initRegitTrip() {
        binding.acceptedTrip.setVisibility(View.GONE);
        binding.noAcceptedTrip.setVisibility(View.VISIBLE);
        binding.regitTripRv.setVisibility(View.GONE);
        binding.noRegitTrip.setVisibility(View.VISIBLE);
        //init recyclerview
        waitingTripRVAdapter = new WaitingTripRVAdapter(new ArrayList<>(), getContext());
        binding.regitTripRv.setAdapter(waitingTripRVAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.regitTripRv.setLayoutManager(layoutManager);
        //observe data
        mViewModel.waitingTrips.observe(getViewLifecycleOwner(), trips -> {
            if(trips != null && trips.size() > 0){
                binding.regitTripRv.setVisibility(View.VISIBLE);
                binding.noRegitTrip.setVisibility(View.GONE);
                waitingTripRVAdapter.setListTrip(trips);
                waitingTripRVAdapter.notifyDataSetChanged();
            }
            else {
                binding.regitTripRv.setVisibility(View.GONE);
                binding.noRegitTrip.setVisibility(View.VISIBLE);
            }
        });
        //on click
        waitingTripRVAdapter.setOnItemClickListener(new WaitingTripRVAdapter.OnItemClickListener() {
            @Override
            public void onCallClick(View view, int position) {
                Trip trip = waitingTripRVAdapter.getListTrip().get(position);
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + trip.getDriver().getContactInfomation().getPhoneNumber()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onMessageClick(View view, int position) {
                Trip trip = waitingTripRVAdapter.getListTrip().get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("sms:" + trip.getDriver().getContactInfomation().getPhoneNumber()));
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }

            @Override
            public void onDeleteClick(View view, int position) {
                Trip trip = waitingTripRVAdapter.getListTrip().get(position);
                mViewModel.cancelTrip(trip);
            }
        });
    }

    private void initButton() {
        //driver start trip
        binding.startTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.start_trip_ques)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // start
                                mViewModel.startTrip();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                // Create the AlertDialog object and show
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //driver delete trip
        binding.deleteTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.delete_trip)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // delete
                                mViewModel.driverCancelTrip(mViewModel.currentTrip.getValue());
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                // Create the AlertDialog object and show
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //click pulbic trip
        binding.publicTripLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DriverTripDetailActivity.class);
                Trip trip = mViewModel.currentTrip.getValue();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TRIP, trip);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //passenger join trip
        binding.joinTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TrackingMapsForPassengerActivity.class);
                Trip trip = mViewModel.acceptedTrip.getValue();
                ClientTrip clientTrip = mViewModel.currentClientTrip.getValue();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TRIP, trip);
                bundle.putSerializable(Constants.CLIENT_TRIP, clientTrip);
                Log.d(TAG, "trip: " + trip.toString());
                Log.d(TAG, "clientTrip: " + clientTrip.toString());
                intent.putExtra(Constants.Bundle, bundle);
                startActivityForResult(intent,Constants.EXECUTE_TRIP_REQUEST_CODE);
            }
        });
        //passenger cancel trip
        binding.cancelTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage(R.string.delete_trip)
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // delete
                                mViewModel.passengerCancelTrip();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null);
                // Create the AlertDialog object and show
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        //unrated trip button
        binding.btnUnratedTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UnratedTripActivity.class);
                startActivity(intent);
            }
        });
    }

    private void observeData() {
        mViewModel.currentTrip.observe(getViewLifecycleOwner(), trip -> {
            if(trip != null && trip.getStartPlace().getPlaceID()!=null){
                binding.publicTrip.setVisibility(View.VISIBLE);
                binding.noPublicTrip.setVisibility(View.GONE);
                if (trip.getTransport()!=null && trip.getTransport().getLicensePlate()!=null){
                    binding.publicLicensePlate.setText("BKS: "+trip.getTransport().getLicensePlate());
                }
                if(trip.getStartPlace().getName() != null){
                    binding.publicFrom.setText(trip.getStartPlace().getName());
                }
                if(trip.getEndPlace().getName() != null){
                    binding.publicTo.setText(trip.getEndPlace().getName());
                }
                if(trip.getStartTime() != null){
                    binding.publicTime.setText("Thời gian: "+CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
                }
                if(trip.getPricePerKm() != null){
                    binding.publicPrice.setText("Giá: "+trip.getPricePerKm().toString()+" VND/km");
                }
                if(trip.getTotalSeat() != null){
                    binding.publicTotalSeat.setText("Số ghế: "+trip.getTotalSeat() + "");
                }
            }else {
                binding.publicTrip.setVisibility(View.GONE);
                binding.noPublicTrip.setVisibility(View.VISIBLE);
            }
        });
        mViewModel.acceptedTrip.observe(getViewLifecycleOwner(), trip -> {
            if(trip != null && trip.getStartPlace().getPlaceID()!=null){
                binding.acceptedTrip.setVisibility(View.VISIBLE);
                binding.noAcceptedTrip.setVisibility(View.GONE);
                if (trip.getTransport()!=null && trip.getTransport().getLicensePlate()!=null){
                    binding.acceptedLicensePlate.setText("BKS: "+trip.getTransport().getLicensePlate());
                }
                if(trip.getStartPlace().getName() != null){
                    binding.acceptedFrom.setText(trip.getStartPlace().getName());
                }
                if(trip.getEndPlace().getName() != null){
                    binding.acceptedTo.setText(trip.getEndPlace().getName());
                }
                if(trip.getStartTime() != null){
                    binding.acceptedTime.setText("Thời gian: "+CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
                }
                if(trip.getPricePerKm() != null){
                    binding.acceptedPrice.setText("Giá: "+trip.getPricePerKm().toString()+" VND/km");
                }
                if(trip.getTotalSeat() != null){
                    binding.acceptedTotalSeat.setText("Số ghế: "+trip.getTotalSeat() + "");
                }
            }else {
                binding.acceptedTrip.setVisibility(View.GONE);
                binding.noAcceptedTrip.setVisibility(View.VISIBLE);
            }
        });
        
        mViewModel.status.observe(getViewLifecycleOwner(), status -> {
            if(status.equals(Constants.SUCCESS)){
                if(mViewModel.message.getValue() != null){
                    if(mViewModel.message.getValue().equals(Constants.START_TRIP_SUCCESS)){
                        //go to tracking activity
                        Intent intent = new Intent(getActivity(), TrackingMapsActivity.class);
                        Trip trip = mViewModel.currentTrip.getValue();
                        List<ClientTrip> clientTrips = mViewModel.clientTrips.getValue();
                        Log.d(TAG, "observeData: - trip"+trip);
                        Log.d(TAG, "observeData: -clienttrip "+clientTrips);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.TRIP, trip);
                        bundle.putSerializable(Constants.LIST_CLIENT_TRIP, new ArrayList<>(clientTrips));
                        bundle.putString(Constants.ROLE, Constants.DRIVER);
                        intent.putExtra(Constants.Bundle, bundle);
                        startActivityForResult(intent,Constants.EXECUTE_TRIP_REQUEST_CODE);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.EXECUTE_TRIP_REQUEST_CODE){
            binding.swipeRefershLayout.setRefreshing(true);
            refreshData();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(YourRidesViewModel.class);
        // TODO: Use the ViewModel
        initRegitTrip();
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private void refreshData(){
        mViewModel.getCurrentTrip();
        mViewModel.getAcceptedTrip();
        mViewModel.getWaitingTrips();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.swipeRefershLayout.setRefreshing(false);
            }
        }, 3000);
    }
}