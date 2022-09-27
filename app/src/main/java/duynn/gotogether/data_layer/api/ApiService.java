package duynn.gotogether.data_layer.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import duynn.gotogether.data_layer.model.model.Account;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    //create GsonConverterFactory
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    //create retrofit
    ApiService apiService = new Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8888/api/v1/")
//            .baseUrl("https://62f1dda225d9e8a2e7d1f848.mockapi.io/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService.class);

    @POST("account/login")
    Call<Boolean> login(@Body Account account);
}
