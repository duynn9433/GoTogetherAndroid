package duynn.gotogether.data_layer.retrofit_client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import duynn.gotogether.data_layer.api.ApiService;
import duynn.gotogether.data_layer.service.AccountService;
import duynn.gotogether.data_layer.service.GoongPlaceService;
import duynn.gotogether.data_layer.service.TripService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static RetrofitClient instance;

    private Gson gson;

    private OkHttpClient okHttpClient;

    private Retrofit retrofit;

    private ApiService apiService;
    private AccountService accountService;
    private TripService tripService;

    public RetrofitClient() {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        okHttpClient = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
//            .baseUrl("http://192.168.0.104:8888/api/v1/")
                .baseUrl("http://192.168.1.130:8888/api/v1/")
//            .baseUrl("http://duynn.loca.lt/api/v1/")
//            .baseUrl("http://10.0.2.2:8888/api/v1/")
//            .baseUrl("https://62f1dda225d9e8a2e7d1f848.mockapi.io/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson)).build();

        apiService = retrofit.create(ApiService.class);
        accountService = retrofit.create(AccountService.class);
        tripService = retrofit.create(TripService.class);
    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }

    public AccountService getAccountService() {
        return accountService;
    }

    public TripService getTripService() {
        return tripService;
    }
}
