package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.User;
import org.jetbrains.annotations.NotNull;

public class SessionManager {
    private Context context;
    private static SessionManager instance;

    private SharedPreferences sharedPreferences;
    private final String USER_TOKEN = "user_token";
    private final String USER_OBJECT = "user_object";
    private final String CLIENT_OBJECT = "client_object";

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

}
