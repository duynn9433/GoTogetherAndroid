package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.User;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class SessionManager {
    private Context context;
    private static SessionManager instance;

    private SharedPreferences sharedPreferences;
    public final static String USER_TOKEN = "user_token";
    public final static String USER_OBJECT = "user_object";
    public final static String CLIENT_OBJECT = "client_object";
    public final static String FCM_TOKEN = "fcm_token";
    public final static String GEOFENCE = "GEOFENCE";

    private SessionManager(@NotNull Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(
                context.getString(R.string.app_name),
                Context.MODE_PRIVATE);
    }

    public static SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }
    public void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }
    public void saveGeofence(String geofence) {
        sharedPreferences.edit().putString(GEOFENCE, geofence).apply();
    }
    public String getGeofence() {
        return sharedPreferences.getString(GEOFENCE, "");
    }
    public void clearGeofence() {
        sharedPreferences.edit().remove(GEOFENCE).apply();
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return sharedPreferences.getString(USER_TOKEN, null);
    }


    public void clearToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_TOKEN);
        editor.apply();
    }

    public void saveFcmToken(String token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FCM_TOKEN, token);
        editor.apply();
    }

    public String getFcmToken() {
        return sharedPreferences.getString(FCM_TOKEN, null);
    }

    public void clearFcmToken() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(FCM_TOKEN);
        editor.apply();
    }

    public void saveUser(User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_OBJECT, new Gson().toJson(user));
        editor.apply();
    }
    public void saveClient(Client client) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CLIENT_OBJECT, new Gson().toJson(client));
        editor.apply();
    }

    public User getUser() {
        String userJson = sharedPreferences.getString(USER_OBJECT, null);
        return new Gson().fromJson(userJson, User.class);
    }

    public Client getClient() {
        String clientJson = sharedPreferences.getString(CLIENT_OBJECT, null);
        return new Gson().fromJson(clientJson, Client.class);
    }

    public void clearUser() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(USER_OBJECT);
        editor.apply();
    }

    public void clearClient() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(CLIENT_OBJECT);
        editor.apply();
    }

    public void logout() {
        clearToken();
        clearUser();
        clearClient();

    }
}
