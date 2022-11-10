package duynn.gotogether.ui_layer.fragment.profile;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.User;
import duynn.gotogether.data_layer.repository.AuthenRepo;
import duynn.gotogether.data_layer.repository.ClientRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class ProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private MutableLiveData<Client> client;
    private SessionManager sessionManager;
    private ClientRepo clientRepo;

    private MutableLiveData<Status> status;

    public ProfileViewModel(@NonNull @NotNull Application application) {
        super(application);
        client = new MutableLiveData<>();
        client.setValue(new Client());
        status = new MutableLiveData<>();
        status.setValue(new Status());
        sessionManager = SessionManager.getInstance(application);
        clientRepo = ClientRepo.getInstance(sessionManager.getToken());
        User user = sessionManager.getUser();
        getClientWithId(user.getId());
    }

    private void getClientWithId(Long id) {
        clientRepo.getClientWithId(id, client, status);
    }

    public void addTransport(Transport transport) {
        clientRepo.addTransport(transport, status, client);
    }
}