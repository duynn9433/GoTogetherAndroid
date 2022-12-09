package duynn.gotogether.ui_layer.activity.chat;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.chat.Message;
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.data_layer.repository.MessageRoomRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatViewModel extends AndroidViewModel {
    //data
    private Client sender;
    private Client receiver;
    private MutableLiveData<List<Message>> messages;
    private MutableLiveData<String> status;
    private MutableLiveData<String> message;
    private SessionManager sessionManager;
    //repository to room db
    private MessageRoomRepo messageRoomRepo;

    public ChatViewModel(@NonNull Application application) {
        super(application);
        messages = new MutableLiveData<>();
        messages.setValue(new ArrayList<>());
        sessionManager = SessionManager.getInstance(application);
        duynn.gotogether.data_layer.model.model.Client myClient = sessionManager.getClient();
        sender = Client.builder()
                .id(myClient.getId())
                .name(myClient.getFullNameString())
                .phone(myClient.getContactInfomation().getPhoneNumber())
                .build();
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        status.setValue("loading");
        message.setValue("Loading...");
        messageRoomRepo = MessageRoomRepo.getInstance(application);
    }

    public void sendMessage(Message message) {
        List<Message> list = messages.getValue();
        list.add(message);
        messages.setValue(list);
        //call to repository to save to db
        messageRoomRepo.saveMessage(message);

        //call to server to send message
    }

    public void saveClient(){
        //call to repository to save to db
        messageRoomRepo.saveClient(receiver);
    }
    public void getAllMessage(){
        //call to repository to get all message from db
        List<Message> list = messageRoomRepo.getMessageBySenderIdAndReceiverId(sender.getId(),receiver.getId());
        messages.setValue(list);
    }
}
