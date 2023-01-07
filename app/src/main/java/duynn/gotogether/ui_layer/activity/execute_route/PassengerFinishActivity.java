package duynn.gotogether.ui_layer.activity.execute_route;

import android.app.NotificationManager;
import android.os.Handler;
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
import org.modelmapper.internal.asm.Handle;

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
        Log.d(TAG, "onCreate: " + bundle);
        long clientTripId = Long.parseLong(bundle.getString(Constants.CLIENT_TRIP_ID));
        long driverId = Long.parseLong(bundle.getString(Constants.DRIVER_ID));
        String distance = bundle.getString(Constants.DISTANCE);
        String price = bundle.getString(Constants.PRICE);
        String passengerNumber = bundle.getString(Constants.PASSENGER_NUM);
        binding.distance.setText("Quãng đường: "+distance+" km");
        binding.price.setText("Thanh toán: "+price+" VND");
        binding.numOfSeat.setText("Số người: "+passengerNumber+" người");
        binding.comment.setText("Tuyệt vời");

        binding.send.setOnClickListener(v -> {
            PassengerFinishRequest passengerFinishRequest = new PassengerFinishRequest();
            //TODO: fix comment
            Comment comment = Comment.builder()
//                    .driver(Client.builder().id(driverId).build())
                    .clientTrip(ClientTrip.builder().id(clientTripId)
                            .distance(Double.parseDouble(distance))
                            .build())
                    .content(binding.comment.getText().toString())
                    .rating((int) binding.rating.getRating())
                    .build();
            passengerFinishRequest.setComment(comment);
            Log.d("PassengerFinishViewModel", "finishTrip: "+passengerFinishRequest);
            viewModel.finishTrip(comment);
            ToastUseCase.showShortToast(this, "Đã gửi đánh giá");
            //CLOSE NOTI
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.cancel(Constants.TRIP_CANCEL_NOTI_ID);
            setResult(RESULT_OK);
            finish();
        });
    }

}