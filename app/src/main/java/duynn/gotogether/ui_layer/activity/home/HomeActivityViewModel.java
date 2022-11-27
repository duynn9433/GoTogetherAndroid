package duynn.gotogether.ui_layer.activity.home;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.repository.ClientRepo;
import duynn.gotogether.data_layer.repository.FirebaseMessageRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class HomeActivityViewModel extends AndroidViewModel {
    MutableLiveData<String> token;
    SessionManager sessionManager;
    ClientRepo clientRepo;

    public HomeActivityViewModel(@NonNull Application application) {
        super(application);
        sessionManager = SessionManager.getInstance(application);
        token = new MutableLiveData<>();
        token.setValue("");
        String token = sessionManager.getToken();
        clientRepo = ClientRepo.getInstance(token);
    }

    public void updateToken() {
        FirebaseMessageRepo.getInstance(getApplication()).getFcmToken(token);
    }

    public void sendTokenToServer(String fcmToken) {
        sessionManager.saveFcmToken(fcmToken);
        Client client = sessionManager.getClient();
        clientRepo.updateFcmToken(fcmToken, client.getId());
    }
}
