package duynn.gotogether;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;

import android.content.Intent;
import android.os.Bundle;

import duynn.gotogether.databinding.ActivityMainBinding;
import duynn.gotogether.ui_layer.activity.home.HomeActivity;
import duynn.gotogether.ui_layer.state_holders.view_model.AccountViewModel;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        AccountViewModel accountViewModel = new AccountViewModel();
        activityMainBinding.setAccountViewModel(accountViewModel);

        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);

    }
}