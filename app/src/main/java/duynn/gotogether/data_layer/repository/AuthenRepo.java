package duynn.gotogether.data_layer.repository;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.helper.error_helper.ApiError;
import duynn.gotogether.data_layer.helper.error_helper.ErrorUtils;
import duynn.gotogether.data_layer.model.dto.request.Authen.LoginReq;
import duynn.gotogether.data_layer.model.dto.response.Authen.LoginResp;
import duynn.gotogether.data_layer.model.dto.response.Authen.RegisterRes;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.User;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.AuthenService;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Stack;

public class AuthenRepo {
    public static final String TAG = AuthenRepo.class.getSimpleName();
    public static AuthenRepo instance;

    private final AuthenService authenService;

    private LoginResp loginResp;

    public static AuthenRepo getInstance() {
        if (instance == null) {
            instance = new AuthenRepo();
        }
        return instance;
    }

    public AuthenRepo() {
        authenService = RetrofitClient.getInstance().createService(AuthenService.class);
    }

    public void login(LoginReq loginReq,
                      MutableLiveData<String> token,
                      MutableLiveData<Client> client,
                      MutableLiveData<String> status,
                      Context context) {
        Call<LoginResp> call = authenService.login(loginReq);

        call.enqueue(new Callback<LoginResp>() {
            @Override
            public void onResponse(Call<LoginResp> call, Response<LoginResp> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse1: " + response.body().toString());
                    loginResp = response.body();
                    token.setValue(loginResp.getJwttoken());
                    client.setValue(loginResp.getClient());
                    status.setValue(Constants.SUCCESS);

                    //TODO: save token and user to shared preference
                    SessionManager.getInstance(context).saveToken(loginResp.getJwttoken());
//                    System.out.println("Client: " + loginResp.getClient().toString());
                    SessionManager.getInstance(context).saveClient(loginResp.getClient());


                } else{
                    try {
                        String errorBody = response.errorBody().string();
                        Log.d(TAG, "error resp: " + errorBody);
                        ApiError apiError = null;
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                        ToastUseCase.showLongToast(context, apiError.getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    status.setValue(Constants.FAIL);

                }
            }

            @Override
            public void onFailure(Call<LoginResp> call, Throwable t) {
                status.setValue(Constants.FAIL);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


    public void register(MutableLiveData<Client> client, MutableLiveData<String> status, MutableLiveData<String> message) {
        User user = new User(client.getValue());
        Call<RegisterRes> call = authenService.register(user);
        Log.d(TAG, "register: " + user.toString());
        call.enqueue(new Callback<RegisterRes>() {
            @Override
            public void onResponse(Call<RegisterRes> call, Response<RegisterRes> response) {
                try{
                    RegisterRes registerRes = response.body();
                    if (response.isSuccessful()) {
//                        Log.d(TAG, "onResponse1: " + response.body().toString());
                        status.setValue(Constants.SUCCESS);
                        message.setValue(registerRes.getMessage());
                    } else if (response.code() == 401) {
//                        Log.d(TAG, "onResponse2: " + response.message());
                        status.setValue(Constants.UNAUTHORIZED);
                        message.setValue(registerRes.getMessage());
                    } else{
//                        Log.d(TAG, "onResponse3: " + response.message());
                        status.setValue(Constants.FAIL);
                        message.setValue(registerRes.getMessage());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "onResponse: " + e.getMessage());
                    status.setValue(Constants.FAIL);
                    message.setValue("Register failed");
                }

            }

            @Override
            public void onFailure(Call<RegisterRes> call, Throwable t) {
                status.setValue(Constants.FAIL);
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    public void checkLogin(MutableLiveData<String> status) {
        Call<Status> call = authenService.checkLogin();
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
//                    Log.d(TAG, "onResponse1: " + response.body().toString());
                    status.setValue(Constants.SUCCESS);
                } else if (response.code() == 401) {
//                    Log.d(TAG, "onResponse2: " + response.message());
                    status.setValue(Constants.UNAUTHORIZED);
                } else{
//                    Log.d(TAG, "onResponse3: " + response.message());
                    status.setValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                status.setValue(Constants.FAIL);
//                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
