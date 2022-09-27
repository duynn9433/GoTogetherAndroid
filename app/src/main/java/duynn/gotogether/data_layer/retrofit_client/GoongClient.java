package duynn.gotogether.data_layer.retrofit_client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import duynn.gotogether.data_layer.api.ApiService;
import duynn.gotogether.data_layer.service.AccountService;
import duynn.gotogether.data_layer.service.GoongPlaceService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoongClient {
    private static GoongClient instance;

    Gson gson ;
    OkHttpClient okHttpClient;
    Retrofit retrofit;
    GoongPlaceService goongPlaceService;

    public GoongClient() {
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl("https://rsapi.goong.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        goongPlaceService = retrofit.create(GoongPlaceService.class);
    }

    public static GoongClient getInstance() {
        if (instance == null) {
            instance = new GoongClient();
        }
        return instance;
    }

    public GoongPlaceService getGoongPlaceService() {
        return goongPlaceService;
    }
}
