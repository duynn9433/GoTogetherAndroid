package duynn.gotogether.ui_layer.service;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import duynn.gotogether.data_layer.model.chat.ChatRequest;
import duynn.gotogether.data_layer.repository.MessageRoomRepo;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.PassengerFinishActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationChannel channel;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        //channel
        channel = new NotificationChannel(
                Constants.FCM_CHANNEL_ID,
                Constants.FCM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        String title = message.getNotification().getTitle();
        if(title != null && title.equals(Constants.PASSENGER_FINISH_TRIP)){
            passengerFinishTrip(message);
        } else if(title != null && title.equals(Constants.TRIP_CANCEL)){
            notiTripCancel(message);
        } else if (title != null && title.equals(Constants.CHAT_MESSAGE)){
            saveChatMessage(message);
        } else{
            String body = message.getNotification().getBody();
            //notification
            Notification.Builder builder = new Notification.Builder(this, Constants.FCM_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, builder.build());
        }


    }

    private void saveChatMessage(RemoteMessage message) {
        Log.d("MyFirebaseMessaging", "saveChatMessage: " + message.getData().get(Constants.CHAT_REQUEST));
        MessageRoomRepo messageRoomRepo = MessageRoomRepo.getInstance(this);

        Map<String, String> data = message.getData();
        String json = data.get(Constants.CHAT_REQUEST);
        ChatRequest chatRequest = new Gson().fromJson(json, ChatRequest.class);
        messageRoomRepo.saveClient(chatRequest.getSender());
        messageRoomRepo.saveMessage(chatRequest.getMessage());
    }

    private void passengerFinishTrip(RemoteMessage message) {
        //pending intent
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, PassengerFinishActivity.class);
        //data
        Map<String, String> data = message.getData();
        Bundle bundle = new Bundle();
        if(data != null){
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        resultIntent.putExtra(Constants.Bundle, bundle);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
        //notification
        Notification.Builder builder = new Notification.Builder(this, Constants.FCM_CHANNEL_ID)
                .setContentTitle("Hoàn thành chuyến đi")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat.from(this).notify(1, builder.build());
    }

    private void notiTripCancel(RemoteMessage message) {
        //pending intent
        // Create an Intent for the activity you want to start
        Intent resultIntent = new Intent(this, PassengerFinishActivity.class);
        //data
        Map<String, String> data = message.getData();
        Bundle bundle = new Bundle();
        if(data != null){
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
        }
        resultIntent.putExtra(Constants.Bundle, bundle);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );
        //notification
        Notification.Builder builder = new Notification.Builder(this, Constants.FCM_CHANNEL_ID)
                .setContentTitle("Đánh giá thái độ")
                .setOngoing(true)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat.from(this).notify(Constants.TRIP_CANCEL_NOTI_ID, builder.build());
    }
}
