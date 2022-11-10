package duynn.gotogether.ui_layer.activity.authen;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.request.Authen.LoginReq;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.repository.AuthenRepo;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class AuthenViewModel extends AndroidViewModel {
    private MutableLiveData<String> token;
    private MutableLiveData<Client> client;

    private MutableLiveData<String> status;
    private MutableLiveData<String> message;

    public AuthenViewModel(@NonNull @NotNull Application application) {
        super(application);
        token = new MutableLiveData<>();
        client = new MutableLiveData<>();
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
    }

    public void login(String username, String password) {
        LoginReq loginReq = new LoginReq(username, password);
        AuthenRepo.getInstance().login(loginReq, token, client, status, getApplication().getApplicationContext());
    }

    public void checkLogin() {
        //TODO:check login
    }

    public void register() {
        AuthenRepo.getInstance().register(client, status, message);
    }
}
