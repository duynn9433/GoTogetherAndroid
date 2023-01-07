package duynn.gotogether.ui_layer.activity.rating;

import android.content.Intent;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.ActivityRatingBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;

public class RatingActivity extends AppCompatActivity {

    private static final String TAG = RatingActivity.class.getSimpleName();
    private ActivityRatingBinding binding;
    private RatingViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(RatingViewModel.class);
        initData();
        initListener();
        observerData();
    }


    private void observerData() {
        viewModel.getComment().observe(this, comment -> {
            if (comment != null) {
                if(comment.getRating()!=null){
                    binding.ratingBar.setRating(comment.getRating());
                }
                if(comment.getContent()!=null){
                    binding.ratingComment.setText(comment.getContent());
                }
                if(comment.getClientTrip()!=null){
                    ClientTrip clientTrip = comment.getClientTrip();
                    if(clientTrip.getTrip()!=null && clientTrip.getClient()!=null) {
                        Trip trip = clientTrip.getTrip();
                        binding.tripNumber.setText("Chuyến đi số: " + trip.getId());
                        Client client = clientTrip.getClient();
                        //me is passenger
                        if (viewModel.getSessionManager().getClient().getId().equals(client.getId())) {
                            viewModel.setPassenger(true);
                            binding.receiver.setText(trip.getDriver().getFullNameString());
                        } else {
                            viewModel.setPassenger(false);
                            binding.receiver.setText(client.getFullNameString());
                        }
                    }
                    if(clientTrip.getPickUpPlace()!=null){
                        binding.from.setText(clientTrip.getPickUpPlace().getName());
                    }
                    if(clientTrip.getDropOffPlace()!=null){
                        binding.to.setText(clientTrip.getDropOffPlace().getName());
                    }
                }

            }
        });
        viewModel.getStatus().observe(this, status -> {
            if (status != null) {
                if (status.equals(Constants.SUCCESS)) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.POSITION, viewModel.getPosition());
                    intent.putExtra(Constants.Bundle, bundle);
                    setResult(RESULT_OK, intent);
                    ToastUseCase.showShortToast(this, "Đánh giá thành công");
                    finish();
                }else{
                    if(!viewModel.getMessage().getValue().isEmpty())
                        ToastUseCase.showShortToast(this, "Đánh giá thất bại");
                }
            }
        });
    }


    private void initListener() {
        binding.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            viewModel.setRating(rating);
        });
        binding.ratingComment.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    viewModel.setCommentContent(binding.ratingComment.getText().toString());
                }
            }
        });
        binding.sendBtn.setOnClickListener(v -> {
            binding.ratingComment.clearFocus();
            viewModel.submitRating();
        });
    }

    private void initData() {
        //--------init data--------------
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        ClientTrip clientTrip = (ClientTrip) bundle.getSerializable(Constants.CLIENT_TRIP);
        Client me = viewModel.getSessionManager().getClient();
        int position = bundle.getInt(Constants.POSITION);
        viewModel.setPosition(position);
        Comment comment = Comment.builder()
                .rating(3)
                .content("Tuyệt vời")
                .sender(me)
                .clientTrip(clientTrip)
                .build();
        //me is passenger
        if(me.getId().equals(clientTrip.getClient().getId())){
            comment.setReceiver(clientTrip.getTrip().getDriver());
            viewModel.setPassenger(true);
        }else{///me is driver
            comment.setReceiver(clientTrip.getClient());
            viewModel.setPassenger(false);
        }
        viewModel.getComment().setValue(comment);
        //-----------------------------------------
    }
}