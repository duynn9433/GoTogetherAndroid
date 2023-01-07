package duynn.gotogether.data_layer.retrofit_client;

import android.content.Context;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import duynn.gotogether.data_layer.api.ApiService;
import duynn.gotogether.data_layer.service.AccountService;
import duynn.gotogether.data_layer.service.TripService;
import duynn.gotogether.domain_layer.CalendarDeserializer;
import duynn.gotogether.domain_layer.CalendarSerializer;
import lombok.Getter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Getter
public class RetrofitClient {
    private static RetrofitClient instance;

    public static final String BASE_URL_LOCAL = "http://192.168.1.170:8888/api/v1/";
//    public static final String BASE_URL_LOCAL = "http://192.168.3.116:8888/api/v1/";
//    public static final String BASE_URL_LOCAL = "http://192.168.132.45:8888/api/v1/";
    public static final String BASE_URL_LOCAL2 = "http://10.0.2.2:8888/api/v1/";
    public static final String BASE_URL_TUNNEL = "http://duynn.loca.lt/api/v1/";

    private static Retrofit.Builder builder;

    private Gson gson;

    private static OkHttpClient.Builder okHttpClient;

    private static HttpLoggingInterceptor loggingInterceptor;
    public static Retrofit retrofit;

    private ApiService apiService;
    private AccountService accountService;
    private TripService tripService;

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    public RetrofitClient() {
        gson = new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .registerTypeAdapter(GregorianCalendar.class, new CalendarSerializer())
                .registerTypeAdapter(GregorianCalendar.class, new CalendarDeserializer())
                .registerTypeAdapter(Calendar.class, new CalendarDeserializer())
                .create();

        loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(6, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor);

        builder = new Retrofit.Builder()
                .baseUrl(BASE_URL_LOCAL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient.build());

        retrofit = builder.build();

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

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceWithToken(Class<S> serviceClass, final String token) {
        if (token != null) {
            okHttpClient.interceptors().clear();
            okHttpClient.addInterceptor(loggingInterceptor)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", token)
                                .build();
                        return chain.proceed(request);
                    });
            builder.client(okHttpClient.build());
            retrofit = builder.build();
        }
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceWithAuth(Class<S> serviceClass, Context context) {
            okHttpClient.interceptors().clear();
            okHttpClient.addInterceptor(loggingInterceptor)
                    .addInterceptor(new AuthInterceptor(context));
            builder.client(okHttpClient.build());
            retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
