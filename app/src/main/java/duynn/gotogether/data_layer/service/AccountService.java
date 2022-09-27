package duynn.gotogether.data_layer.service;

import java.util.List;

import duynn.gotogether.data_layer.model.model.Account;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AccountService {
    @POST("account/login")
    Call<Boolean> login(@Body Account account);

    @GET("account")
    Call<List<Account>> getAll();
}
