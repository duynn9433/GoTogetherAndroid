package duynn.gotogether.domain_layer.common;

import com.google.android.libraries.places.api.model.Place;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class Constants {
    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 1;
    public static final int REQUEST_BACKGROUND_LOCATION_PERMISSIONS_CODE = 2;
    public static final int GET_START_POINT = 3;
    public static final int GET_END_POINT = 4;
    public static final int GET_LOCATION_FROM_MAP_GET_PLACE = 5;
    public static final int START_LOCATION_REQUEST_CODE = 6;
    public static final int END_LOCATION_REQUEST_CODE = 7;
    public static final int STOP_LOCATION_REQUEST_CODE = 8;
    public static final int ADD_TRANSPORT_REQUEST_CODE = 9;
    public static final int GET_START_PLACE_FROM_SEARCH_REQUEST_CODE = 10;
    public static final int GET_END_PLACE_FROM_SEARCH_REQUEST_CODE = 11;
    public static final int FINISH_TRIP_REQUEST_CODE = 12;
    public static final int PASSENGER_REQUEST_CODE = 13;

    public static final String ACTION_SERVICE_START = "ACTION_SERVICE_START";
    public static final String ACTION_SERVICE_STOP = "ACTION_SERVICE_STOP";
    public static final String ACTION_NAVIGATE_TO_TRACKER_ACTIVITY = "ACTION_NAVIGATE_TO_TRACKER_ACTIVITY";

    public static final String TRACKER_NOTIFICATION_CHANNEL_ID = "TRACKER_NOTIFICATION_CHANNEL_ID";
    public static final String TRACKER_NOTIFICATION_CHANNEL_NAME = "TRACKER_NOTIFICATION_CHANNEL_NAME";
    public static final int TRACKER_NOTIFICATION_ID = 3;

    public static final int PENDING_INTENT_REQUEST_CODE = 99;

    public static final long LOCATION_UPDATE_INTERVAL = 4000L; // 4 seconds
    public static final long LOCATION_UPDATE_FASTEST_INTERVAL = 2000L; // 2 seconds

    public static final float DEFAULT_ZOOM = 15f;
    public static final float DEFAULT_ZOOM_FOR_TRACKER = 18f;
    public static final float DEFAULT_ZOOM_FOR_WORLD = 1f;
    public static final float DEFAULT_ZOOM_FOR_LANDMASS = 5f;
    public static final float DEFAULT_ZOOM_FOR_CITY = 10f;
    public static final float DEFAULT_ZOOM_FOR_STREET = 15f;
    public static final float DEFAULT_ZOOM_FOR_BUILDING = 20f;

    public static final int DEFAULT_ANIMATION_TIME = 1000;

    //for navigation activity
    public static final String Bundle = "Bundle";
    public static final String GeocodingResult = "GeocodingResult";
    public static final String Position = "Position";
    public static final String TRANSPORT = "Transport";
    public static final String TRIP = "TRIP";
    public static final String START_TRIP_SUCCESS = "START_TRIP_SUCCESS";
    public static final String ROLE = "ROLE";
    public static final String DRIVER = "DRIVER";
    public static final String LIST_CLIENT_TRIP = "LIST_CLIENT_TRIP";
    public static final String GOOGLE_MAPS_TRAVEL_MODE_DRIVING = "driving";
    public static final String GOOGLE_MAPS_TRAVEL_MODE_WALKING = "walking";
    public static final String GOOGLE_MAPS_TRAVEL_MODE_BICYCLING = "bicycling";
    public static final String CLIENT_TRIP = "CLIENT_TRIP";
    public static final String DISTANCE = "DISTANCE";
    public static final String PRICE = "PRICE";
    public static final String PASSENGER_NUM = "PASSENGER_NUM";
    public static final float GEOFENCE_RADIUS = 100;

    public static final String USER_TOKEN = "user_token";
    public static final String USER_OBJECT = "user_object";
    public static final String CLIENT_OBJECT = "client_object";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String GEOFENCE = "GEOFENCE";
    public static final String PASSENGER_FINISH_TRIP = "PASSENGER_FINISH_TRIP";
    public static final String TITLE = "TITLE";
    public static final String CLIENT_TRIP_ID = "CLIENT_TRIP_ID";
    public static final String DRIVER_ID = "DRIVER_ID";

    public static final String LAST_DISTANCE = "LAST_DISTANCE";
    public static final String POSITION = "POSITION";
    public static final String TIME = "TIME";
    public static final String FINISH_TRIP_SUCCESS = "FINISH_TRIP_SUCCESS";
    public static final String TRIP_CANCEL = "TRIP_CANCEL";
    public static final int TRIP_CANCEL_NOTI_ID = 100;
    public static final int PASSENGER_FINISH_TRIP_REQUEST_CODE = 101;
    public static final String START_DISTANCE = "START_DISTANCE";
    public static final String END_DISTANCE = "END_DISTANCE";
    public static final int EXECUTE_TRIP_REQUEST_CODE = 102;

    public static String TRIPS = "TRIPS";
    public static final String GOONG_PLACE_DETAIL_RESULT = "GOONG_PLACE_DETAIL_RESULT";

    //for get place activity
    public static final List<Place.Field> placeFields = Arrays.asList(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG);


    //status
    public static final String SUCCESS = "Success";
    public static final String FAIL = "Fail";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";


    public static final String EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String PHONE_REGEX = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";

    public static final String SEARCH_TRIP_REQUEST = "SEARCH_TRIP_REQUEST";
    public static final String FCM_CHANNEL_ID = "FIREBASE_MESSAGE_NOTIFICATION";
    public static final CharSequence FCM_CHANNEL_NAME = "FIREBASE_MESSAGE_NOTIFICATION_NAME";

    //FOR DIRECTION
    public static String VEHICLE_CAR = "car";
    public static String VEHICLE_BIKE = "bike";
    public static String VEHICLE_TAXI = "taxi";
    public static String VEHICLE_TRUCK = "truck";
    public static String VEHICLE_HD = "hd"; //(for ride hailing vehicles)
}

