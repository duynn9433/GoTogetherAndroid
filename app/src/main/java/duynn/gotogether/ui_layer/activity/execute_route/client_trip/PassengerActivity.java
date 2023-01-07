package duynn.gotogether.ui_layer.activity.execute_route.client_trip;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.fcm.FcmNotificationsSender;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.ContactInfomation;
import duynn.gotogether.data_layer.model.model.Fullname;
import duynn.gotogether.databinding.ActivityPassengerBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsViewModel;

import java.util.ArrayList;
import java.util.List;

public class PassengerActivity extends AppCompatActivity {
    ActivityPassengerBinding binding;
    private List<ClientTrip> clientTrips;
    private PassengerRVAdapter adapter;
    private TrackingMapsViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassengerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(TrackingMapsViewModel.class);
        //get data
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        clientTrips = (List<ClientTrip>) bundle.getSerializable(Constants.LIST_CLIENT_TRIP);
        //fake data
//        clientTrips = new ArrayList<>();
//        fakeData();
        //init recyclerview
        initRecyclerView();
    }

    private void initRecyclerView() {
        adapter = new PassengerRVAdapter(clientTrips, this);
        binding.passengerRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.passengerRecyclerView.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new PassengerRVAdapter.OnItemClickListener() {
            @Override
            public void onFinishClick(View view, int position) {
                //TODO: call api to finish trip
//                viewModel.requestFinishPassenger(clientTrips.get(position));
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CLIENT_TRIP, clientTrips.get(position));
                bundle.putInt(Constants.POSITION, position);
                ToastUseCase.showLongToast(PassengerActivity.this, "Finish trip"+position);
                Log.d("PassengerActivity", "onFinishClick: " +position+" - "+ clientTrips.get(position));
                Intent data = new Intent();
                data.putExtras(bundle);
                setResult(RESULT_OK, data);
                // gọi hàm finish() để đóng Activity hiện tại và trở về MainActivity.
                finish();
//                FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(
//                        clientTrips.get(position).getClient().getFcmToken()
//                        , Constants.PASSENGER_FINISH_TRIP
//                        , Constants.PASSENGER_FINISH_TRIP, getApplication());
//                fcmNotificationsSender.SendNotifications();
//                ToastUseCase.showLongToast(getApplication(), "Finish trip success");
            }

            @Override
            public void onCallClick(View view, int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Client client = clientTrips.get(position).getClient();
                intent.setData(Uri.parse("tel:" + client.getContactInfomation().getPhoneNumber()));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

}