package duynn.gotogether.data_layer.repository;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.google.gson.Gson;
import duynn.gotogether.data_layer.dao.ChattedClientDao;
import duynn.gotogether.data_layer.dao.ClientDao;
import duynn.gotogether.data_layer.dao.MessageDao;
import duynn.gotogether.data_layer.fcm.FcmNotificationsSender;
import duynn.gotogether.data_layer.model.chat.*;
import duynn.gotogether.data_layer.room_database.MessageDatabase;
import duynn.gotogether.domain_layer.common.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageRoomRepo {
    private static final String TAG = MessageRoomRepo.class.getSimpleName();
    private static MessageRoomRepo instance;
    private MessageDatabase messageDatabase;
    private SessionManager sessionManager;

    public static MessageRoomRepo getInstance(Context context) {
        if (instance == null) {
            instance = new MessageRoomRepo(context);
        }
        return instance;
    }

    public MessageRoomRepo(Context context) {
        messageDatabase = MessageDatabase.getInstance(context);
        sessionManager = SessionManager.getInstance(context);
    }

    public void saveMessage(Message message) {
        //save to db
        MessageDao messageDao = messageDatabase.messageDao();
        messageDao.insert(message);
        //send to server

    }
    public void sendToTarget(ChatRequest chatRequest, Application application) {
        FcmNotificationsSender fcmNotificationsSender =
                new FcmNotificationsSender(chatRequest.getReceiver().getFcmToken()
                        , Constants.CHAT_MESSAGE
                        , Constants.CHAT_MESSAGE
                        , application
                );
        String json = new Gson().toJson(chatRequest);
        Map<String, String> data = new HashMap<>();
        data.put(Constants.CHAT_REQUEST, json);
        Log.d(TAG, "sendToTarget: " + json);
        fcmNotificationsSender.setData(data);
        fcmNotificationsSender.SendNotifications();
    }

    public boolean isClientExist(Long clientId){
        ClientDao clientDao = messageDatabase.clientDao();
        List<Client> clients = clientDao.getById(clientId);
        return clients!=null && !clients.isEmpty();
    }
    public boolean isChattedClientExist(Long clientId){
        ChattedClientDao chattedClientDao = messageDatabase.chattedClientDao();
        List<ChattedClient> lis = chattedClientDao.getByOwnerIdAndClientId(sessionManager.getClient().getId(),clientId);
        return lis!=null && !lis.isEmpty();
    }

    public List<ChattedClientFull> getClientByOwnerId(Long ownerId){
        ChattedClientDao chattedClientDao = messageDatabase.chattedClientDao();
        return chattedClientDao.getByOwnerId(ownerId);
    }
    public List<Message> getMessageBySenderIdAndReceiverId(Long senderId, Long receiverId){
        MessageDao messageDao = messageDatabase.messageDao();
        return messageDao.getBySenderIdAndReceiverId(senderId, receiverId);
    }
    public void saveClient(Client client){
        if(!isClientExist(client.getId())){
            //save client
            ClientDao clientDao = messageDatabase.clientDao();
            clientDao.insert(client);
        }else {
            //update client
            ClientDao clientDao = messageDatabase.clientDao();
            clientDao.update(client);
        }
        if(!isChattedClientExist(client.getId())){
            //save chatted client
            ChattedClient chattedClient = ChattedClient.builder()
                    .ownerId(sessionManager.getClient().getId())
                    .clientId(client.getId())
                    .build();
            saveChattedClient(chattedClient);
        }
    }
    public void saveChattedClient(ChattedClient chattedClient){
        ChattedClientDao chattedClientDao = messageDatabase.chattedClientDao();
        chattedClientDao.insert(chattedClient);
    }


}
