package duynn.gotogether.ui_layer.activity.authen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.request.Authen.LoginReq;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.repository.AuthenRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.AuthenService;
import duynn.gotogether.domain_layer.common.Constants;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
@Setter
public class AuthenViewModel extends AndroidViewModel {
    private MutableLiveData<String> token;
    private MutableLiveData<Client> client;

    private MutableLiveData<String> status;
    private MutableLiveData<String> message;
    SessionManager sessionManager;

    public AuthenViewModel(@NonNull @NotNull Application application) {
        super(application);
        token = new MutableLiveData<>();
        client = new MutableLiveData<>();
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        sessionManager = SessionManager.getInstance(application);
    }

    public void login(String username, String password) {
        LoginReq loginReq = new LoginReq(username, password);
        AuthenRepo.getInstance().login(loginReq, token, client, status, getApplication().getApplicationContext());
    }

    public void checkLogin() {
        String token = sessionManager.getToken();
        if(token != null) {
            AuthenService authenService = RetrofitClient.getInstance().createServiceWithToken(AuthenService.class, token);
            authenService.checkLogin().enqueue(new Callback<Status>() {
                @Override
                public void onResponse(Call<Status> call, Response<Status> response) {
                    if(response.isSuccessful()) {
                        status.setValue(Constants.SUCCESS);
                    } else {
                        status.setValue(Constants.FAIL);
                    }
                }
                @Override
                public void onFailure(Call<Status> call, Throwable t) {
                    status.setValue(Constants.FAIL);
                }
            });
        }
    }

    public void register() {
        AuthenRepo.getInstance().register(client, status, message);
    }
}
