package duynn.gotogether.ui_layer.activity.execute_route.geofence;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsActivity;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = GeofenceBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
//        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence: geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
            SessionManager.getInstance(context).saveGeofence(geofence.getRequestId());
        }
//        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

//        switch (transitionType) {
//            case Geofence.GEOFENCE_TRANSITION_ENTER:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", TrackingMapsActivity.class);
//                break;
//            case Geofence.GEOFENCE_TRANSITION_DWELL:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", TrackingMapsActivity.class);
//                break;
//            case Geofence.GEOFENCE_TRANSITION_EXIT:
//                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
//                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "", TrackingMapsActivity.class);
//                break;
//        }

    }
}