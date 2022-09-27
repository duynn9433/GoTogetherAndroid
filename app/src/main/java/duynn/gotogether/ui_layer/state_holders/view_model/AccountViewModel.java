package duynn.gotogether.ui_layer.state_holders.view_model;

import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.List;

import duynn.gotogether.BR;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.api.ApiService;
import duynn.gotogether.data_layer.model.model.Account;
import duynn.gotogether.data_layer.service.AccountService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountViewModel extends BaseObservable {
    private String username;
    private String password;

    @Bindable
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        notifyPropertyChanged(BR.username);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    public void onCLickLogin(){
        RetrofitClient retrofitClient = new RetrofitClient();
        ApiService apiService = retrofitClient.getApiService();
        Log.e("AccountViewModel", "onCLickLogin");
        Account account = new Account(getUsername(), getPassword());
        Log.e("AccountViewModel", "account: " + account.toString());
//        apiService.login(account).enqueue(new Callback<Boolean>() {
//            @Override
//            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
//                if (response.isSuccessful()) {
//                    if (response.body()) {
//                        // login success
//                        Log.e("Login", "Login success");
//                    } else {
//                        // login fail
//                        Log.e("Login", "Login fail");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Boolean> call, Throwable t) {
//                Log.e("Login", "No response");
//            }
//        });

        AccountService accountService = retrofitClient.getAccountService();
        accountService.getAll().enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                if (response.isSuccessful()) {
                    List<Account> accounts = response.body();
                    for (Account account : accounts) {
                        Log.e("AccountViewModel2", "account: " + account.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Log.e("AccountViewModel2", "No response" );
                t.printStackTrace();

            }
        });

    }
}
