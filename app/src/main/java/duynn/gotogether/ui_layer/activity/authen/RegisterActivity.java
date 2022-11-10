package duynn.gotogether.ui_layer.activity.authen;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.User;
import duynn.gotogether.databinding.ActivityRegisterBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = RegisterActivity.class.getSimpleName();
    private ActivityRegisterBinding binding;
    private AuthenViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        viewModel = new ViewModelProvider(this).get(AuthenViewModel.class);
        viewModel.getClient().setValue(new Client());
        viewModel.getToken().setValue("token");

        binding.setAuthenViewModel(viewModel);
        binding.setLifecycleOwner(this);

        initButton();
        observeViewModel();

    }

    private void observeViewModel() {
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case Constants.SUCCESS:
                    ToastUseCase.showLongToast(this, "Register success");
//                    Log.d(TAG, "Register success");
                    Intent intent = new Intent(this, CongratulationActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case Constants.FAIL:
                    ToastUseCase.showLongToast(this, viewModel.getMessage().getValue());
//                    Log.d(TAG, "Register fail");
                    break;
            }
        });

    }

    private void initButton() {
        binding.cancel.setOnClickListener(v -> {
            v.requestFocus();
            finish();
        });
        binding.register.setOnClickListener(v -> {
            v.requestFocus();
//            viewModel.register();
            if(!checkNotNull() && !checkPassword() && !checkValidData()){
                Log.d(TAG, viewModel.getClient().getValue().toString());
                viewModel.register();
            }else{
                Toast.makeText(this, "Điền lại thông tin không chính xác", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkValidData() {
        boolean isInvalid = false;
        if(!binding.email.getText().toString().matches(Constants.EMAIL_REGEX)){
            binding.email.setError("Email không hợp lệ");
            isInvalid = true;
        }
        if(!binding.phone.getText().toString().matches(Constants.PHONE_REGEX)){
            binding.phone.setError("Số điện thoại không hợp lệ");
            isInvalid = true;
        }
        return isInvalid;
    }

    private boolean checkPassword() {
        if(!binding.password.getText().toString().equals(binding.confirmPassword.getText().toString())){
            binding.password.setError("Password not match");
            binding.confirmPassword.setError("Password not match");
            return true;
        }
        return false;
    }

    private boolean checkNotNull() {
        boolean isNull = false;
        if(binding.username.getText().toString().isEmpty()){
            binding.username.setError("Không được để trống");
            isNull = true;
        }
        if(binding.password.getText().toString().isEmpty()){
            binding.password.setError("Không được để trống");
            isNull = true;
        }
        if(binding.confirmPassword.getText().toString().isEmpty()){
            binding.confirmPassword.setError("Không được để trống");
            isNull = true;
        }
        if(binding.email.getText().toString().isEmpty()){
            binding.email.setError("Không được để trống");
            isNull = true;
        }
        if(binding.phone.getText().toString().isEmpty()){
            binding.phone.setError("Không được để trống");
            isNull = true;
        }
        if(binding.firstName.getText().toString().isEmpty()){
            binding.firstName.setError("Không được để trống");
            isNull = true;
        }
        if(binding.lastName.getText().toString().isEmpty()){
            binding.lastName.setError("Không được để trống");
            isNull = true;
        }
        if(binding.city.getText().toString().isEmpty()){
            binding.city.setError("Không được để trống");
            isNull = true;
        }
        return isNull;

    }
}