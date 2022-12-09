package duynn.gotogether.ui_layer.fragment.inbox;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.chat.ChattedClientFull;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.data_layer.repository.MessageRoomRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class InboxViewModel extends AndroidViewModel {
    private MutableLiveData<List<Client>> clients;
    private SessionManager sessionManager;
    //repository to room db
    private MessageRoomRepo messageRoomRepo;


    public InboxViewModel(@NonNull @NotNull Application application) {
        super(application);
        clients = new MutableLiveData<>();
        clients.setValue(new ArrayList<>());
        sessionManager = SessionManager.getInstance(application);
        messageRoomRepo = MessageRoomRepo.getInstance(application);
    }

    public void getClientsFromRoomDB(){
        //call to repository to get from db
        List<ChattedClientFull> list = messageRoomRepo.getClientByOwnerId(sessionManager.getClient().getId());
        List<Client> clients = list.stream().map(ChattedClientFull::getClient).collect(Collectors.toList());
        this.clients.setValue(clients);
    }
}