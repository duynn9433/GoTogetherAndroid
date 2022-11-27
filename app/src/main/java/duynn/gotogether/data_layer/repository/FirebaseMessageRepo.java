package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.fcm.FcmNotificationsSender;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.domain_layer.DistanceUseCase;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.execute_route.TrackingMapsActivity;
import duynn.gotogether.ui_layer.activity.execute_route.client_trip.PassengerActivity;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class FirebaseMessageRepo {
    private static final String TAG = FirebaseMessageRepo.class.getSimpleName();
    public static FirebaseMessageRepo instance;
    private Context context;

    public static FirebaseMessageRepo getInstance(Context context) {
        if (instance == null) {
            instance = new FirebaseMessageRepo(context);
        }
        return instance;
    }

    public FirebaseMessageRepo(Context context) {
        this.context = context;
    }

    public void getFcmToken(MutableLiveData<String> token) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            token.postValue("");
                            return;
                        }
                        // Get new FCM registration token
                        String tokenResult = task.getResult();
                        token.postValue(tokenResult);
                        // Log
                        Log.d(TAG, tokenResult);
                    }
                });
    }

    public void requestFinishPassenger(ClientTrip clientTrip, int passengerNumb,
                                       double distance,
                                       double price,
                                       long clientTripId,
                                       long driverId,
                                       MutableLiveData<String> status,
                                       MutableLiveData<String> message) {
        Map<String, String> data = new HashMap<>();
        data.put(Constants.PRICE, DistanceUseCase.formatToString2digitEndPoint(price));
        data.put(Constants.DISTANCE, DistanceUseCase.formatToString2digitEndPoint(distance));
        data.put(Constants.PASSENGER_NUM, String.valueOf(passengerNumb));
        data.put(Constants.CLIENT_TRIP_ID, String.valueOf(clientTripId));
        data.put(Constants.DRIVER_ID, String.valueOf(driverId));

        FcmNotificationsSender fcmNotificationsSender = new FcmNotificationsSender(
                clientTrip.getClient().getFcmToken()
                , Constants.PASSENGER_FINISH_TRIP
                , Constants.PASSENGER_FINISH_TRIP, context );
        fcmNotificationsSender.setData(data);
        fcmNotificationsSender.SendNotifications();
        ToastUseCase.showLongToast(context, "Finish trip success");

    }
}
