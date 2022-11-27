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
        binding.distance.setText(bundle.getString(Constants.DISTANCE));
        binding.time.setText(bundle.getString(Constants.TIME));

        binding.finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, null);
                finish();
            }
        });

    }
}