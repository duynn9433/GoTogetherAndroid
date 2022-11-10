package duynn.gotogether.ui_layer.activity.authen;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityCongratulationBinding;

public class CongratulationActivity extends AppCompatActivity {

    private ActivityCongratulationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCongratulationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.6));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;
        getWindow().setAttributes(params);

        binding.button.setOnClickListener(v -> {
            finish();
        });

    }
}