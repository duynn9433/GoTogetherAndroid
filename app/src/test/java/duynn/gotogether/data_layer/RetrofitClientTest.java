package duynn.gotogether.data_layer;

import android.util.Log;

import junit.framework.TestCase;

import org.junit.Test;

import java.io.IOException;

import duynn.gotogether.data_layer.api.ApiService;
import duynn.gotogether.data_layer.model.model.Account;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitClientTest extends TestCase {
    @Test
    public void testRetrofitClient() throws IOException {
        RetrofitClient retrofitClient = new RetrofitClient();
        assertNotNull(retrofitClient);
        ApiService apiService = retrofitClient.getApiService();
        Log.e("print", apiService.login(new Account("duynn", "123456")) + "");
        Response response = apiService.login(new Account("duynn", "123456")).execute();
        assertTrue(response.isSuccessful());

        apiService.login(new Account("duynn", "123456")).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    if (response.body()) {
                        // login success
                        Log.e("Login", "Login success");
                    } else {
                        // login fail
                        Log.e("Login", "Login fail");
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("Login", "No response");
            }
        });
    }

}