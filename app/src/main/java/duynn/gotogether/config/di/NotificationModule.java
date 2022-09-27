package duynn.gotogether.config.di;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.android.scopes.ServiceScoped;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.service.TrackerService;

@Module
@InstallIn(ServiceComponent.class)
public class NotificationModule {

    @ServiceScoped
    @Provides
    public PendingIntent provideTrackerPendingIntent(@ApplicationContext Context context){
        return  PendingIntent.getActivities(
                context,
                Constants.PENDING_INTENT_REQUEST_CODE,
                new Intent[]{
                        //TODO: navigate from main activity
                        new Intent(context, TrackerService.class)
                                .setAction(Constants.ACTION_NAVIGATE_TO_TRACKER_ACTIVITY)
                },
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @ServiceScoped
    @Provides
    public NotificationCompat.Builder provideTrackerNotificationBuilder(
            @ApplicationContext Context context,
            PendingIntent trackerPendingIntent){
        return new NotificationCompat.Builder(context, Constants.TRACKER_NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(false)
                .setOngoing(true)
                .setContentIntent(trackerPendingIntent);
    }

    @ServiceScoped
    @Provides
    public NotificationManager provideNotificationManager(@ApplicationContext Context context){
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
}
