package duynn.gotogether.ui_layer.activity.rating;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UnratedTripViewModel extends AndroidViewModel {
    private static final String TAG = UnratedTripViewModel.class.getSimpleName();
    private MutableLiveData<String> message;
    private MutableLiveData<String> status;
    private MutableLiveData<List<ClientTrip>> clientTripList;
    private SessionManager sessionManager;
    private ClientTripRepo clientTripRepo;

    public UnratedTripViewModel(@NonNull @NotNull Application application) {
        super(application);
        message = new MutableLiveData<>();
        status = new MutableLiveData<>();
        clientTripList = new MutableLiveData<>();
        message.setValue("");
        status.setValue("");
        clientTripList.setValue(new ArrayList<>());
        sessionManager = SessionManager.getInstance(application);
        clientTripRepo = ClientTripRepo.getInstance(sessionManager.getToken());
    }

    public void getUnratedClientTrip() {
        clientTripRepo.getUnratedClientTrip(sessionManager.getClient().getId(), status, message, clientTripList);
    }
}
