package duynn.gotogether.ui_layer.service;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.PassengerFinishActivity;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        String title = message.getNotification().getTitle();
        if(title.equals(Constants.PASSENGER_FINISH_TRIP)){
            NotificationChannel channel = new NotificationChannel(
                    Constants.FCM_CHANNEL_ID,
                    Constants.FCM_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
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
        }else{
            String body = message.getNotification().getBody();
            //channel
            NotificationChannel channel = new NotificationChannel(
                    Constants.FCM_CHANNEL_ID,
                    Constants.FCM_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            //notification
            Notification.Builder builder = new Notification.Builder(this, Constants.FCM_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, builder.build());
        }


    }
}