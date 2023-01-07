package duynn.gotogether.ui_layer.activity.execute_route;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityDriverFinishBinding;
import duynn.gotogether.domain_layer.common.Constants;

public class DriverFinishActivity extends AppCompatActivity {
    ActivityDriverFinishBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverFinishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        binding.distance.setText("Khoảng cách: "+bundle.getString(Constants.DISTANCE) + " km");
        binding.time.setText("Thời gian: "+bundle.getString(Constants.TIME));
        binding.passengerNum.setText("Số lượng khách: "+bundle.getString(Constants.PASSENGER_NUM));

        binding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });

    }
}