package duynn.gotogether.ui_layer.activity.execute_route;

import android.util.Log;
import android.widget.RatingBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.request.PassengerFinishRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.databinding.ActivityPassengerFinishBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;

public class PassengerFinishActivity extends AppCompatActivity {
    private static final String TAG = PassengerFinishActivity.class.getSimpleName();
    private ActivityPassengerFinishBinding binding;
    private PassengerFinishViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPassengerFinishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(PassengerFinishViewModel.class);

        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        long clientTripId = Long.parseLong(bundle.getString(Constants.CLIENT_TRIP_ID));
        long driverId = Long.parseLong(bundle.getString(Constants.DRIVER_ID));
        String distance = bundle.getString(Constants.DISTANCE);
        String price = bundle.getString(Constants.PRICE);
        String passengerNumber = bundle.getString(Constants.PASSENGER_NUM);
        binding.distance.setText("Quãng đường: "+distance+" km");
        binding.price.setText("Thanh toán: "+price+" VND");
        binding.numOfSeat.setText("Số người: "+passengerNumber+" người");

        binding.send.setOnClickListener(v -> {
            PassengerFinishRequest passengerFinishRequest = new PassengerFinishRequest();
            Comment data = Comment.builder()
                    .driver(Client.builder().id(driverId).build())
                    .clientTrip(ClientTrip.builder().id(clientTripId).build())
                    .content(binding.comment.getText().toString())
                    .rating((int) binding.rating.getRating())
                    .build();
            passengerFinishRequest.setComment(data);
            Log.d("PassengerFinishViewModel", "finishTrip: "+passengerFinishRequest);
            viewModel.finishTrip(passengerFinishRequest);
            ToastUseCase.showShortToast(this, "Đã gửi đánh giá");
            finish();
        });
    }

}