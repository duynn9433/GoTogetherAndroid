package duynn.gotogether.ui_layer.activity.authen;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.ActivityLoginBinding;
import duynn.gotogether.ui_layer.activity.PermissionActivity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProvider;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private AuthenViewModel authenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        authenViewModel = new ViewModelProvider(this).get(AuthenViewModel.class);

        authenViewModel.checkLogin();
        initButton();

        observeViewModel();


//        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
//        AccountViewModel accountViewModel = new AccountViewModel();
//        activityMainBinding.setAccountViewModel(accountViewModel);
    }

    private void initButton() {
        binding.btnLogin.setOnClickListener(v -> {
            SessionManager sessionManager = SessionManager.getInstance(this);
            sessionManager.clearClient();
            sessionManager.clearUser();
            sessionManager.clearToken();

            String username = binding.username.getText().toString();
            String password = binding.password.getText().toString();
            if(username.isEmpty() || password.isEmpty()){
                ToastUseCase.showLongToast(this, "Username or password is empty");
            }else {
                authenViewModel.login(username, password);
            }
        });
        binding.btnRegister.setOnClickListener(v -> {
            //TODO: delete pref

            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void observeViewModel() {
        authenViewModel.getStatus().observe(this, status -> {
            switch (status) {
                case Constants.SUCCESS:
                    //TODO: go to next activity
                    ToastUseCase.showLongToast(this, "Login success");
                    Intent intent = new Intent(this, PermissionActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case Constants.FAIL:
                    ToastUseCase.showLongToast(this, "Login fail");
                    break;
                case Constants.UNAUTHORIZED:
                    ToastUseCase.showLongToast(this, "Unauthorized");
                    break;
            }
        });
        authenViewModel.getToken().observe(this, token -> {
            //TODO: save to shared preference
        });
        authenViewModel.getClient().observe(this, user -> {
            //TODO: save to shared preference
        });
    }
}