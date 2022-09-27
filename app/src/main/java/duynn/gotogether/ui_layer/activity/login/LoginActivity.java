package duynn.gotogether.ui_layer.activity.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityMainBinding;
import duynn.gotogether.ui_layer.state_holders.view_model.AccountViewModel;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        AccountViewModel accountViewModel = new AccountViewModel();
        activityMainBinding.setAccountViewModel(accountViewModel);
    }
}