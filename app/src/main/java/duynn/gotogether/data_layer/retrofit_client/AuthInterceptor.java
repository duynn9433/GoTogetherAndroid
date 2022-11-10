package duynn.gotogether.data_layer.retrofit_client;

import android.content.Context;
import duynn.gotogether.data_layer.repository.SessionManager;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class AuthInterceptor implements Interceptor {
    private SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        this.sessionManager = SessionManager.getInstance(context);
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request.Builder requestBuilder =  chain.request().newBuilder();

        String token = sessionManager.getToken();
        if(token != null){
            requestBuilder.addHeader("Authorization", token);
        }

        return null;
    }
}
